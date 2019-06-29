package api.controller

import api.data.dto.ErrorMesssage
import api.data.exception.ErrorType
import api.data.exception.PageNotFoundException
import api.data.exception.PageRetrievalException
import api.data.repository.RepositoryInterface
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.security.InvalidParameterException

class DictionaryController : KoinComponent {

    private val repository: RepositoryInterface by inject()

    fun getNumberOfPagesInDictionary(): Any {
        return try {
            repository.getNumberOfPagesInDictionary()
        } catch (e: PageRetrievalException) {
            ErrorMesssage(message = e.errorMessage, type = ErrorType.PAGE_RETRIVAL_ERROR)
        }
    }

    fun getPageInDictionary(pageNo: Int): Any {
        return try {
            repository.getPageInDictionary(pageNo)
        } catch (e: PageNotFoundException) {
            ErrorMesssage(message = e.errorMessage, type = ErrorType.PAGE_NOT_FOUND)
        } catch (e: PageRetrievalException) {
            ErrorMesssage(message = e.errorMessage, type = ErrorType.PAGE_RETRIVAL_ERROR)
        } catch (e: InvalidParameterException){
            ErrorMesssage(message = e.message?:"Invalid Parameter", type = ErrorType.INVALID_PARAMETER_ERROR)
        }
    }

    fun searchForWordsInDictionary(pageNo: Int, query: String): Any {
        return try {
            repository.searchForWordsInDictionary(pageNo, query)
        } catch (e: PageNotFoundException) {
            ErrorMesssage(message = e.errorMessage, type = ErrorType.PAGE_NOT_FOUND)
        } catch (e: PageRetrievalException) {
            ErrorMesssage(message = e.errorMessage, type = ErrorType.PAGE_RETRIVAL_ERROR)
        } catch (e: InvalidParameterException){
            ErrorMesssage(message = e.message?:"Invalid Parameter", type = ErrorType.INVALID_PARAMETER_ERROR)
        }
    }

}