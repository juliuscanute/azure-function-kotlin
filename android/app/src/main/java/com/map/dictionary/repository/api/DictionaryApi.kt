package com.map.dictionary.repository.api

import com.map.dictionary.repository.dto.Dictionary
import com.map.dictionary.repository.dto.Page
import com.map.dictionary.repository.dto.SearchResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Call

interface DictionaryApi {
    @GET("/v1/dictionary/pages")
    fun getNumberOfPagesInDictionary(): Call<Page>

    @GET("/v1/dictionary/page/{pageNo}")
    fun getWordsInDictionaryPage(@Path("pageNo") pageNo: Int): Call<Dictionary>

    @GET("/v1/dictionary")
    fun searchForWordsInDictionary(@Query("word") word: String, @Query("pageNo") pageNo: Int): Call<SearchResult>
}