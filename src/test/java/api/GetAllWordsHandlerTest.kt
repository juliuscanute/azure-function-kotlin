package api

import api.handler.GetAllWordsHandler
import com.microsoft.azure.functions.*

import java.util.*
import java.util.logging.Logger

import org.junit.jupiter.api.Test
import org.mockito.Mockito.*


/**
 * Unit test for GetAllWordsHandler class.
 */
class GetAllWordsHandlerTest {
    /**
     * Unit test for HttpTriggerJava method.
     */
    @Test
    @Throws(Exception::class)
    fun testHttpTriggerJava() {
        // Setup
        val req = mock(HttpRequestMessage::class.java)

        val queryParams = HashMap<String, String>()
        queryParams["name"] = "Azure"
        doReturn(queryParams).`when`(req as HttpRequestMessage<Optional<String>>).queryParameters

        val queryBody = Optional.empty<String>()
        doReturn(queryBody).`when`(req).body

        doAnswer { invocation ->
            val status = invocation.arguments[0] as HttpStatus
            HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status)
        }.`when`(req).createResponseBuilder(any(HttpStatus::class.java))

        val context = mock(ExecutionContext::class.java)
        doReturn(Logger.getGlobal()).`when`(context).logger

        // Invoke
        val ret = GetAllWordsHandler().run(req, context)

        // Verify
//        assertEquals(ret.status, HttpStatus.OK)
    }
}
