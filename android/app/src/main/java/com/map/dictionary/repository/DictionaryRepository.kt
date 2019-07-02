package com.map.dictionary.repository

import com.map.dictionary.repository.api.DictionaryApi
import com.map.dictionary.repository.dto.Dictionary
import com.map.dictionary.repository.dto.Page
import com.map.dictionary.repository.dto.SearchResult
import com.map.dictionary.repository.exception.FetchException
import retrofit2.HttpException
import retrofit2.Response


class DictionaryRepository(val apiCLient: DictionaryApi) : Repository {

    override fun getNumberOfPagesInDictionary(): Page {
        var page = Page()
        try {
            val call = apiCLient.getNumberOfPagesInDictionary()
            val result = call.execute()
            if (isRequestNotSuccessfull(result)) {
                throw HttpException(result)
            }
            page = result.body()
        } catch (e: Exception) {
            throw FetchException("Unable to fetch total pages")
        }
        return page
    }

    override fun getPageInDictionary(pageNo: Int): Dictionary {
        var dictionary = Dictionary()
        try {
            val call = apiCLient.getWordsInDictionaryPage(pageNo)
            val result = call.execute()
            if (isRequestNotSuccessfull(result)) {
                throw HttpException(result)
            }
            dictionary = result.body()
        } catch (e: Exception) {
            throw FetchException("Unable to fetch requested page")
        }
        return dictionary
    }

    override fun searchForWordsInDictionary(pageNo: Int, query: String): SearchResult {
        var searchResult = SearchResult()
        try {
            val call = apiCLient.searchForWordsInDictionary(pageNo, query)
            val result = call.execute()
            if (isRequestNotSuccessfull(result)) {
                throw HttpException(result)
            }
            searchResult = result.body()
        } catch (e: Exception) {

        }
        return searchResult
    }

    private fun isRequestNotSuccessfull(result: Response<*>) =
        !(result.isSuccessful && result.code() == 200 && result.body() != null)
}