package api

import api.data.dto.Dictionary
import api.data.dto.Meaning
import api.data.dto.Page
import api.data.dto.SearchResult
import api.data.exception.PageNotFoundException
import api.data.repository.RepositoryInterface

class MockRepository : RepositoryInterface {
    override fun getNumberOfPagesInDictionary(): Page {
        return Page(1, 3, 10)
    }

    override fun getPageInDictionary(pageNo: Int): Dictionary {
        return when (pageNo) {
            1 -> Dictionary(1, arrayListOf(Meaning(1, "A", "A")))
            2 -> Dictionary(2, arrayListOf(Meaning(2, "B", "B")))
            else -> throw PageNotFoundException("ERROR")
        }
    }

    override fun searchForWordsInDictionary(pageNo: Int, query: String): SearchResult {
        return when (pageNo) {
            1 -> SearchResult(1,1,2,1, arrayListOf(Meaning(1, "A", "A")))
            2 -> SearchResult(1,1,2,1, arrayListOf(Meaning(2, "B", "B")))
            else -> throw PageNotFoundException("ERROR")
        }
    }
}