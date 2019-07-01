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

class DictionarySearchHandler {
    private val global = Startup

    @FunctionName("searchForWordsInDictionary")
    fun run(
            @HttpTrigger(name = "req", methods = [HttpMethod.GET],
                    route = "v1/dictionary", authLevel = AuthorizationLevel.ANONYMOUS)
            request: HttpRequestMessage<Optional<String>>, context: ExecutionContext): HttpResponseMessage {
        context.logger.info("Processing get words in a dictionary page")
        val query = request.queryParameters
        val word = query["word"] ?: ""
        val pageNo = query["pageNo"]?.toInt() ?: 0
        val result = DictionaryController().searchForWordsInDictionary(pageNo,word)
        return getResponse(request, result)
    }
}