package api.handler

import api.controller.DictionaryController
import api.di.Startup
import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.HttpMethod
import com.microsoft.azure.functions.HttpRequestMessage
import com.microsoft.azure.functions.HttpResponseMessage
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.BindingName
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger
import java.util.*

class DictionaryWordsInPageHandler {
    private val global = Startup

    @FunctionName("getWordsInDictionaryPage")
    fun run(
            @HttpTrigger(name = "req", methods = [HttpMethod.GET], route = "v1/dictionary/page/{pageNo}", authLevel = AuthorizationLevel.ANONYMOUS) request: HttpRequestMessage<Optional<String>>,
            @BindingName("pageNo") pageNo: Int, context: ExecutionContext): HttpResponseMessage {
        context.logger.info("Processing get words in a dictionary page")
        val result = DictionaryController().getPageInDictionary(pageNo)
        return getResponse(request, result)
    }
}