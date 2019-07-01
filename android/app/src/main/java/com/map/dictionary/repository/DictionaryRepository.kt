package com.map.dictionary.repository

import android.util.Log
import com.map.dictionary.repository.api.DictionaryApi
import com.map.dictionary.repository.dto.Dictionary

class DictionaryRepository(val apiCLient: DictionaryApi) : Repository {

    override fun getNumberOfPagesInDictionary() {
        val call = apiCLient.getNumberOfPagesInDictionary()
        val result = call.execute()
    }

    override fun getPageInDictionary(pageNo: Int): Dictionary {
        Log.d("DictionaryRepository", "Request Backend -- Start")
        val call = apiCLient.getWordsInDictionaryPage(pageNo)
        val result = call.execute()
        Log.d("DictionaryRepository", "Request Backend -- End")
        return result.body()
    }

    override fun searchForWordsInDictionary(pageNo: Int, query: String) {
        val call = apiCLient.searchForWordsInDictionary(pageNo, query)
        val result = call.execute()
    }

}