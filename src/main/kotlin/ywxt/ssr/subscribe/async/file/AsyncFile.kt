package ywxt.ssr.subscribe.async.file

import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousFileChannel
import java.nio.channels.CompletionHandler
import java.nio.file.Path
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AsyncFile(val path: String) : Closeable, AutoCloseable {
    private val fileChannel = AsynchronousFileChannel.open(Path.of(path))


    suspend fun read(): ByteArray {
        var buffer: ByteBuffer
        val bf = ByteArrayOutputStream(fileChannel.size().toInt())
        var position = 0L
        do {
            buffer = ByteBuffer.allocate(128)
            fileChannel.read()
        }
    }

    override fun close() {
        if (fileChannel.isOpen) {
            fileChannel.close()
        }
    }

    suspend fun AsynchronousFileChannel.asyncRead(position: Long, buffer: ByteBuffer): Int =
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
}
