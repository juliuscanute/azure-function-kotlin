package api.controller

import api.data.dto.ErrorMesssage
import api.data.exception.ErrorType
import api.data.exception.PageNotFoundException
import api.data.exception.PageRetrievalException
import api.data.repository.RepositoryInterface
import org.koin.core.KoinComponent
import org.koin.core.inject

class DictionaryController: KoinComponent {

    private val repository: RepositoryInterface by inject()

    fun getNumberOfPagesInDictionary() : Any {
        return try {
            repository.getNumberOfPagesInDictionary()
        }catch (e: PageRetrievalException){
            ErrorMesssage(message = e.errorMessage,type = ErrorType.PAGE_RETRIVAL_ERROR)
        }
    }

    fun getPageInDictionary(pageNo: Int) : Any {
        return try {
            repository.getPageInDictionary(pageNo)
        }catch (e: PageNotFoundException){
            ErrorMesssage(message = e.errorMessage,type = ErrorType.PAGE_NOT_FOUND)
        }catch (e: PageRetrievalException){
            ErrorMesssage(message = e.errorMessage,type = ErrorType.PAGE_RETRIVAL_ERROR)
        }
    }

}