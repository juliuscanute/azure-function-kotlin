package api.data.repository

import api.data.dto.Dictionary
import api.data.dto.Page
import api.data.exception.PageRetrievalException
import org.koin.core.KoinComponent

interface RepositoryInterface : KoinComponent {
    fun getNumberOfPagesInDictionary() : Page
    fun getPageInDictionary(pageNo: Int) : Dictionary
    fun searchForWordsInDictionary(query: String) : Dictionary
}