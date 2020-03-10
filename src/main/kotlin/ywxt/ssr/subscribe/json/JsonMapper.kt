package ywxt.ssr.subscribe.json

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

val JSON_MAPPER = jacksonObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)