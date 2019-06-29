package api.handler

import api.data.dto.Dictionary
import api.data.dto.ErrorMesssage
import api.data.dto.Page
import api.data.exception.ErrorType
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.microsoft.azure.functions.HttpRequestMessage
import com.microsoft.azure.functions.HttpResponseMessage
import com.microsoft.azure.functions.HttpStatus
import java.util.*

fun getResponse(request: HttpRequestMessage<Optional<String>>, input: Any): HttpResponseMessage {
    return when (input) {
        is Page -> {
            val jsonConverter = Gson()
            request.createResponseBuilder(HttpStatus.OK).body(jsonConverter.toJson(input)).header("Content-Type", "application/json").header("Access-Control-Allow-Origin", "*").build()
        }
        is Dictionary -> {
            val jsonConverter = Gson()
            request.createResponseBuilder(HttpStatus.OK).body(jsonConverter.toJson(input)).header("Content-Type", "application/json").header("Access-Control-Allow-Origin", "*").build()
        }
        else -> {
            val jsonConverter = GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create()
            val error = input as ErrorMesssage
            error.path = request.uri.path
            if (error.type == ErrorType.PAGE_NOT_FOUND)
                request.createResponseBuilder(HttpStatus.NOT_FOUND).body(jsonConverter.toJson(input)).header("Content-Type", "application/json").header("Access-Control-Allow-Origin", "*").build()
            else
                request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonConverter.toJson(input)).header("Content-Type", "application/json").header("Access-Control-Allow-Origin", "*").build()
        }
    }
}