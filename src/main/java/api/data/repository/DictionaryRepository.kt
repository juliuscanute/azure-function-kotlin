package api.data.repository

import api.data.dto.Dictionary
import api.data.dto.Page
import api.data.exception.PageRetrievalException
import com.microsoft.azure.documentdb.DocumentClient
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.Exception


class DictionaryRepository : RepositoryInterface, KoinComponent {
    companion object {
        val RECORDS_PER_PAGE = 20
    }
    val client: DocumentClient by inject()

    override fun getNumberOfPagesInDictionary(): Page {
        var page: Page
        try {
            val document = client.
                    queryDocuments(
                            System.getenv("COLLECTION_URI"),
                            "SELECT COUNT(1) as TotalRecords FROM c", null).queryIterable.first()
            val total = document.getInt("TotalRecords")
            val endPage = total / RECORDS_PER_PAGE
            page = Page(1,endPage,RECORDS_PER_PAGE)
        }catch (e: Exception){
            throw PageRetrievalException(e.message ?: "Unable to retrieve data")
        }
        return page
    }

    override fun getPageInDictionary(pageNo: Int): Dictionary {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun searchForWordsInDictionary(query: String): Dictionary {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}