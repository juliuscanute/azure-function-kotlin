package api

import api.data.dto.ErrorMesssage
import api.data.dto.Page
import api.data.exception.PageRetrievalException
import api.data.repository.RepositoryInterface
import api.handler.DictionaryHandler
import com.google.gson.Gson
import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.HttpRequestMessage
import com.microsoft.azure.functions.HttpStatus
import org.junit.jupiter.api.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.mockito.Mockito.*
import java.net.URI
import java.util.*
import java.util.logging.Logger
import kotlin.test.assertEquals

class DictionaryHandlerTest {

    @Test
    fun testGetTotalPagesInDictionary() {
        val handler = DictionaryHandler()
        val gson = Gson()
        loadKoinModules(module(override = true) {
            single { MockRepository() as RepositoryInterface }
        })

        val req = mock(HttpRequestMessage::class.java)

        val context = mock(ExecutionContext::class.java)
        doReturn(Logger.getGlobal()).`when`(context).logger
        doAnswer { invocation ->
            val status = invocation.arguments[0] as HttpStatus
            HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status)
        }.`when`(req).createResponseBuilder(any(HttpStatus::class.java))

        val ret = handler.run(req as HttpRequestMessage<Optional<String>>, context)
        val body = ret.body.toString()
        val result = gson.fromJson(body, Page::class.java)
        assertEquals(1, result.start)
        assertEquals(3, result.end)
        assertEquals(10, result.recordsPerPage)
        assertEquals(ret.status, HttpStatus.OK)
    }

    @Test
    fun testGetTotalPagesInDictionary_WithExcetption() {
        val handler = DictionaryHandler()
        val repository = mock(RepositoryInterface::class.java)
        val gson = Gson()
        // Setup
        loadKoinModules(module(override = true) {
            single { repository }
        })

        val req = mock(HttpRequestMessage::class.java)

        doReturn(URI("v1/dictionary/pages")).`when`(req).uri

        val context = mock(ExecutionContext::class.java)
        doReturn(Logger.getGlobal()).`when`(context).logger
        doAnswer { invocation ->
            val status = invocation.arguments[0] as HttpStatus
            HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status)
        }.`when`(req).createResponseBuilder(any(HttpStatus::class.java))

        doAnswer {
            throw PageRetrievalException("error")
        }.`when`(repository).getNumberOfPagesInDictionary()

        val ret = handler.run(req as HttpRequestMessage<Optional<String>>, context)
        val body = ret.body.toString()
        val result = gson.fromJson(body, ErrorMesssage::class.java)
        assertEquals("error",result.message)
        assertEquals("v1/dictionary/pages",result.path)
    }
}
