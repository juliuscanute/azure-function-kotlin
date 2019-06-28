package api

import api.data.dto.Dictionary
import api.data.dto.Page
import api.data.repository.RepositoryInterface

class MockRepository : RepositoryInterface {
    override fun getNumberOfPagesInDictionary(): Page {
        return Page(1, 3, 10)
    }

    override fun getPageInDictionary(pageNo: Int): Dictionary {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun searchForWordsInDictionary(query: String): Dictionary {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}