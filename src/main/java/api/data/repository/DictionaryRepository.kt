package api.data.repository

import api.data.dto.Dictionary
import api.data.dto.Meaning
import api.data.dto.Page
import api.data.dto.SearchResult
import api.data.exception.PageNotFoundException
import api.data.exception.PageRetrievalException
import com.microsoft.azure.documentdb.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.security.InvalidParameterException
import kotlin.math.ceil


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
                    "SELECT Dictionary.total AS TotalRecords FROM Dictionary WHERE Dictionary.partition = 'atoz' AND Dictionary.total > 0", null).queryIterable.first()
            val total = document.getInt("TotalRecords")
            val endPage = ceil(total.toFloat() / RECORDS_PER_PAGE.toFloat()).toInt()
            page = Page(1, endPage, RECORDS_PER_PAGE)
        } catch (e: Exception) {
            throw PageRetrievalException(e.message ?: "Unable to retrieve data")
        }
        return page
    }

    override fun getPageInDictionary(pageNo: Int): Dictionary {
        if (pageNo <= 0) throw InvalidParameterException("Parameter should be greater than 0")
        var page: Dictionary
        try {
            val offset = if ((pageNo - 1) == 0) 0 else (pageNo - 1) * RECORDS_PER_PAGE
            val querySpec = SqlQuerySpec().apply {
                queryText = "SELECT Dictionary.word AS word, Dictionary.meaning AS meaning FROM Dictionary WHERE Dictionary.partition = 'atoz' AND Dictionary.id != 'index' OFFSET @offset LIMIT @records_per_page"
                parameters = SqlParameterCollection().apply {
                    add(SqlParameter("@offset", offset))
                    add(SqlParameter("@records_per_page", RECORDS_PER_PAGE))
                }
            }
            val document = client.queryDocuments(
                    System.getenv("COLLECTION_URI"),
                    querySpec, null)
                    .queryIterable.toList()
            val meanings = document.mapIndexed { index, document ->
                Meaning(id = (index + 1) + offset,
                        word = document.getString("word")?:"",
                        meaning = document.getString("meaning")?:"")
            }.toList()
            if (meanings.isEmpty())
                throw PageNotFoundException(errorMessage = "There is no data left to retrieve")
            page = Dictionary(currentPage = pageNo, words = meanings)
        } catch (e: Exception) {
            throw PageRetrievalException(e.message ?: "Unable to retrieve data")
        }
        return page
    }

    override fun searchForWordsInDictionary(pageNo: Int, query: String): SearchResult {
        if (pageNo <= 0) throw InvalidParameterException("Parameter should be greater than 0")
        if (query.isEmpty()) throw InvalidParameterException("Input search parameter must not be empty")
        var page: SearchResult
        try {
            val offset = if ((pageNo - 1) == 0) 0 else (pageNo - 1) * RECORDS_PER_PAGE

            val findSpec = SqlQuerySpec().apply {
                queryText = "SELECT Dictionary.word AS word, Dictionary.meaning AS meaning FROM Dictionary WHERE Dictionary.partition = @tag AND contains(Dictionary.search, @query) OFFSET @offset LIMIT @records_per_page"
                parameters = SqlParameterCollection().apply {
                    add(SqlParameter("@tag", query.first().toUpperCase().toString()))
                    add(SqlParameter("@query", query.toLowerCase()))
                    add(SqlParameter("@offset", offset))
                    add(SqlParameter("@records_per_page", RECORDS_PER_PAGE))
                }
            }
            val countSpec = SqlQuerySpec().apply {
                queryText = "SELECT COUNT(1) AS TotalRecords FROM Dictionary WHERE Dictionary.partition = @tag AND contains(Dictionary.search, @query)"
                parameters = SqlParameterCollection().apply {
                    add(SqlParameter("@tag", query.first().toUpperCase().toString()))
                    add(SqlParameter("@query", query.toLowerCase()))
                }
            }
            val document = client.queryDocuments(
                    System.getenv("COLLECTION_URI"),
                    findSpec, null)
                    .queryIterable.toList()

            val countDocument = client.queryDocuments(
                    System.getenv("COLLECTION_URI"),
                    countSpec, null).queryIterable.first()

            val total = countDocument.getInt("TotalRecords")
            val endPage = ceil(total.toFloat() / RECORDS_PER_PAGE.toFloat()).toInt()

            val meanings = document.mapIndexed { index, document ->
                Meaning(id = (index + 1) + offset,
                        word = document.getString("word")?:"",
                        meaning = document.getString("meaning")?:"")
            }.toList()


            if (meanings.isEmpty())
                throw PageNotFoundException(errorMessage = "There is no data left to retrieve")

            page = SearchResult(startPage = 1, endPage = endPage, totalRecords = total, currentPage = pageNo, words = meanings)
        } catch (e: Exception) {
            throw PageRetrievalException(e.message ?: "Unable to retrieve data")
        }
        return page
    }
}