package api.handler

import api.controller.DictionaryController
import api.di.Startup
import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.HttpMethod
import com.microsoft.azure.functions.HttpRequestMessage
import com.microsoft.azure.functions.HttpResponseMessage
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger
import java.util.*

class DictionaryTotalPageHandler {

    private val global = Startup

    @FunctionName("getNumberOfPagesInDictionary")
    fun run(
            @HttpTrigger(name = "req", methods = [HttpMethod.GET], route = "v1/dictionary/pages", authLevel = AuthorizationLevel.ANONYMOUS) request: HttpRequestMessage<Optional<String>>,
            context: ExecutionContext): HttpResponseMessage {
        context.logger.info("Processing get pages of dictionary")
        val result = DictionaryController().getNumberOfPagesInDictionary()
        return getResponse(request, result)
    }
}
