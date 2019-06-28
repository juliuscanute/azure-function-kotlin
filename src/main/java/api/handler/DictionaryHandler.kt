package api.handler

import api.controller.DictionaryController
import api.data.dto.ErrorMesssage
import api.data.dto.Page
import api.di.Startup
import com.google.gson.Gson
import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger
import java.util.*

class DictionaryHandler {

    private val global = Startup
    private val jsonConverter = Gson()

    @FunctionName("getNumberOfPagesInDictionary")
    fun run(
            @HttpTrigger(name = "req", methods = [HttpMethod.GET], route = "v1/dictionary/pages", authLevel = AuthorizationLevel.ANONYMOUS) request: HttpRequestMessage<Optional<String>>,
            context: ExecutionContext): HttpResponseMessage {
        context.logger.info("Processing get pages of dictionary")
        val result = DictionaryController().getNumberOfPagesInDictionary()

        return when (result) {
            is Page -> request.createResponseBuilder(HttpStatus.OK).body(jsonConverter.toJson(result)).header("Content-Type", "application/json").header("Access-Control-Allow-Origin", "*").build()
            else -> {
                (result as ErrorMesssage).path = request.uri.path
                request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonConverter.toJson(result)).header("Content-Type", "application/json").header("Access-Control-Allow-Origin", "*").build()
            }
        }

    }
}
