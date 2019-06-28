package api.controller

import api.data.Dictionary
import api.data.repository.RepositoryInterface
import org.koin.core.KoinComponent
import org.koin.core.inject

class DictionaryController: KoinComponent {

    private val repository: RepositoryInterface by inject()

    fun getWords(currentPage: Int): Dictionary {
        return repository.getWords(currentPage)
    }

}