package api.handler

import api.controller.DictionaryController
import api.di.Startup
import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger
import org.koin.core.context.startKoin
import java.util.*

class GetAllWordsHandler {

    init {
        startKoin { modules(Startup.dictionary) }
    }

    @FunctionName("words")
    fun run(
            @HttpTrigger(name = "req", methods = [HttpMethod.GET], route = "v1/words", authLevel = AuthorizationLevel.ANONYMOUS) request: HttpRequestMessage<Optional<String>>,
            context: ExecutionContext): HttpResponseMessage {
        context.logger.info("Java HTTP trigger processed a request.")
        val controller = DictionaryController().getWords(1)
        return request.createResponseBuilder(HttpStatus.OK).body(controller).build()

    }
}
