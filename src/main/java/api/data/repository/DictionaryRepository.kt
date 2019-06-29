package api.data.repository

import api.data.dto.Dictionary
import api.data.dto.Meaning
import api.data.dto.Page
import api.data.exception.PageNotFoundException
import api.data.exception.PageRetrievalException
import com.microsoft.azure.documentdb.DocumentClient
import com.microsoft.azure.documentdb.SqlParameter
import com.microsoft.azure.documentdb.SqlParameterCollection
import com.microsoft.azure.documentdb.SqlQuerySpec
import org.koin.core.KoinComponent
import org.koin.core.inject


class DictionaryRepository : RepositoryInterface, KoinComponent {
    companion object {
        val RECORDS_PER_PAGE = 20
    }

    val client: DocumentClient by inject()

    override fun getNumberOfPagesInDictionary(): Page {
        var page: Page
        try {
            val document = client.queryDocuments(
                    System.getenv("COLLECTION_URI"),
                    "SELECT COUNT(1) as TotalRecords FROM c", null).queryIterable.first()
            val total = document.getInt("TotalRecords")
            val endPage = (total / RECORDS_PER_PAGE) + 1
            page = Page(1, endPage, RECORDS_PER_PAGE)
        } catch (e: Exception) {
            throw PageRetrievalException(e.message ?: "Unable to retrieve data")
        }
        return page
    }

    override fun getPageInDictionary(pageNo: Int): Dictionary {
        var page: Dictionary
        try {
            val offset = if ((pageNo - 1) == 0) 0 else (pageNo - 1) * RECORDS_PER_PAGE
            val querySpec = SqlQuerySpec().apply {
                queryText = "SELECT c.id AS id, c.word AS word, c.meaning AS meaning FROM c OFFSET @offset LIMIT @records_per_page"
                parameters = SqlParameterCollection().apply {
                    add(SqlParameter("@offset", offset))
                    add(SqlParameter("@records_per_page", RECORDS_PER_PAGE))
                }
            }
            val document = client.queryDocuments(
                    System.getenv("COLLECTION_URI"),
                    querySpec, null)
                    .queryIterable.toList()

            val meanings = document.map { document ->
                Meaning(id = document.getString("id"),
                        word = document.getString("word"),
                        meaning = document.getString("meaning")) }.toList()

            if(meanings.isEmpty())
                throw PageNotFoundException(errorMessage = "There is no data left to retrieve")

            page = Dictionary(currentPage = pageNo,words = meanings)
        } catch (e: Exception) {
            throw PageRetrievalException(e.message ?: "Unable to retrieve data")
        }
        return page
    }

    override fun searchForWordsInDictionary(query: String): Dictionary {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}