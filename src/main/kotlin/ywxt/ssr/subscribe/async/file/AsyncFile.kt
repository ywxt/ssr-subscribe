package ywxt.ssr.subscribe.async.file

import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.File
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousFileChannel
import java.nio.channels.CompletionHandler
import java.nio.charset.Charset
import java.nio.file.OpenOption
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AsyncFile(val path: String,vararg options:OpenOption) : Closeable, AutoCloseable {
    private val fileChannel = AsynchronousFileChannel.open(
        Path.of(path),
        *options
    )


    suspend fun read(): ByteArray {
        val buffer: ByteBuffer = ByteBuffer.allocate(128)
        val bf = ByteArrayOutputStream(fileChannel.size().toInt())
        var position = 0L
        do {
            buffer.clear()
            val length = fileChannel.asyncRead(position, buffer)
            buffer.flip()
            while (buffer.hasRemaining()) {
                bf.write(buffer.get().toInt())
            }
            position += length
        } while (length != -1)
        return bf.toByteArray()
    }

    suspend fun readString(): String = String(read(), Charset.forName("UTF-8"))

    suspend fun write(data: ByteArray) {
        var position = 0
        var buffer: ByteBuffer
        do {
            val writeLength = if ((data.size - position) / 128 == 0) data.size - position else 128
            buffer = ByteBuffer.wrap(data, position, writeLength)
            fileChannel.asyncWrite(position.toLong(), buffer)
            position += writeLength
        } while (position < data.size)

    }

    suspend fun writeString(data: String) = write(data.toByteArray())

    override fun close() {
        if (fileChannel.isOpen) {
            fileChannel.close()
        }
    }

    private suspend fun AsynchronousFileChannel.asyncRead(position: Long, buffer: ByteBuffer): Int =
        suspendCancellableCoroutine { cont ->
            read(buffer, position, Unit, object : CompletionHandler<Int, Unit> {
                override fun completed(p0: Int, p1: Unit) {
                    cont.resume(p0)
                }

                override fun failed(p0: Throwable, p1: Unit) {
                    cont.resumeWithException(p0)
                }
            })

        }

    private suspend fun AsynchronousFileChannel.asyncWrite(position: Long, buffer: ByteBuffer): Int =
        suspendCancellableCoroutine { cont ->
            write(buffer, position, Unit, object : CompletionHandler<Int, Unit> {
                override fun completed(result: Int, attachment: Unit?) {
                    cont.resume(result)
                }

                override fun failed(exc: Throwable, attachment: Unit?) {
                    cont.resumeWithException(exc)
                }

            })
        }
}
