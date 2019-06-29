package api

import api.data.dto.Dictionary
import api.data.dto.ErrorMesssage
import api.data.dto.Page
import api.data.dto.SearchResult
import api.data.exception.PageRetrievalException
import api.data.repository.RepositoryInterface
import api.handler.DictionarySearchHandler
import api.handler.DictionaryTotalPageHandler
import api.handler.DictionaryWordsInPageHandler
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

class DictionaryTotalPageHandlerTest {

    @Test
    fun testGetTotalPagesInDictionary() {
        val handler = DictionaryTotalPageHandler()
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
    fun testGetTotalPagesInDictionary_withError() {
        val handler = DictionaryTotalPageHandler()
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
        assertEquals("error", result.message)
        assertEquals("v1/dictionary/pages", result.path)
    }

    @Test
    fun testGetWordsInAPage() {
        val handler = DictionaryWordsInPageHandler()
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

        val ret = handler.run(req as HttpRequestMessage<Optional<String>>, 2, context)

        val body = ret.body.toString()
        val result = gson.fromJson(body, Dictionary::class.java)
        assertEquals(2, result.currentPage)
        assertEquals("B", result.words.first().word)
        assertEquals("B", result.words.first().meaning)
        assertEquals(ret.status, HttpStatus.OK)
    }

    @Test
    fun testGetWordsInAPage_queryError() {
        val handler = DictionaryWordsInPageHandler()
        val gson = Gson()
        loadKoinModules(module(override = true) {
            single { MockRepository() as RepositoryInterface }
        })

        val req = mock(HttpRequestMessage::class.java)
        doReturn(URI("v1/dictionary")).`when`(req).uri

        val context = mock(ExecutionContext::class.java)
        doReturn(Logger.getGlobal()).`when`(context).logger
        doAnswer { invocation ->
            val status = invocation.arguments[0] as HttpStatus
            HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status)
        }.`when`(req).createResponseBuilder(any(HttpStatus::class.java))

        val ret = handler.run(req as HttpRequestMessage<Optional<String>>, -1, context)

        val body = ret.body.toString()
        val result = gson.fromJson(body, ErrorMesssage::class.java)
        assertEquals(ret.status, HttpStatus.NOT_FOUND)
    }

    @Test
    fun testSearchInDictionary() {
        val handler = DictionarySearchHandler()
        val gson = Gson()
        loadKoinModules(module(override = true) {
            single { MockRepository() as RepositoryInterface }
        })

        val req = mock(HttpRequestMessage::class.java)

        val queryParams = HashMap<String, String>()
        queryParams["word"] = "test"
        queryParams["pageNo"] = "1"
        doReturn(queryParams).`when`(req as HttpRequestMessage<Optional<String>>).queryParameters

        val context = mock(ExecutionContext::class.java)
        doReturn(Logger.getGlobal()).`when`(context).logger
        doAnswer { invocation ->
            val status = invocation.arguments[0] as HttpStatus
            HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status)
        }.`when`(req).createResponseBuilder(any(HttpStatus::class.java))

        val ret = handler.run(req as HttpRequestMessage<Optional<String>>, 2, context)

        val body = ret.body.toString()
        val result = gson.fromJson(body, SearchResult::class.java)
        assertEquals(1, result.currentPage)
        assertEquals("A", result.words.first().word)
        assertEquals("A", result.words.first().meaning)
        assertEquals(ret.status, HttpStatus.OK)
    }


    @Test
    fun testSearchInDictionary_emptyInput() {
        val handler = DictionarySearchHandler()
        val gson = Gson()
        loadKoinModules(module(override = true) {
            single { MockRepository() as RepositoryInterface }
        })

        val req = mock(HttpRequestMessage::class.java)

        val queryParams = HashMap<String, String>()
        doReturn(queryParams).`when`(req as HttpRequestMessage<Optional<String>>).queryParameters

        doReturn(URI("v1/dictionary")).`when`(req).uri

        val context = mock(ExecutionContext::class.java)
        doReturn(Logger.getGlobal()).`when`(context).logger
        doAnswer { invocation ->
            val status = invocation.arguments[0] as HttpStatus
            HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status)
        }.`when`(req).createResponseBuilder(any(HttpStatus::class.java))

        val ret = handler.run(req as HttpRequestMessage<Optional<String>>, 2, context)

        val body = ret.body.toString()
        val result = gson.fromJson(body, SearchResult::class.java)
        assertEquals(ret.status, HttpStatus.NOT_FOUND)
    }
}
