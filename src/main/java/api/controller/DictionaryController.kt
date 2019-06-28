package api.controller

import api.data.dto.ErrorMesssage
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
            ErrorMesssage(message = e.errorMessage)
        }
    }

}