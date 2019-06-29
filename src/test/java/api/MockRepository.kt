package api

import api.data.dto.Dictionary
import api.data.dto.Meaning
import api.data.dto.Page
import api.data.exception.PageNotFoundException
import api.data.repository.RepositoryInterface

class MockRepository : RepositoryInterface {
    override fun getNumberOfPagesInDictionary(): Page {
        return Page(1, 3, 10)
    }

    override fun getPageInDictionary(pageNo: Int): Dictionary {
        return when (pageNo) {
            1 -> Dictionary(1, arrayListOf(Meaning("A", "A", "A")))
            2 -> Dictionary(2, arrayListOf(Meaning("A", "B", "B")))
            else -> throw PageNotFoundException("ERROR")
        }
    }

    override fun searchForWordsInDictionary(query: String): Dictionary {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}