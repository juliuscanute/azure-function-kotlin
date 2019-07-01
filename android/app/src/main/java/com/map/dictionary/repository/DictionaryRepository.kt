package com.map.dictionary.repository

import com.map.dictionary.repository.api.DictionaryApi

class DictionaryRepository(val apiCLient: DictionaryApi) : Repository {

    override fun getNumberOfPagesInDictionary() {
        val call = apiCLient.getNumberOfPagesInDictionary()
        val result = call.execute()
    }

    override fun getPageInDictionary(pageNo: Int) {
        val call = apiCLient.getWordsInDictionaryPage(pageNo)
        val result = call.execute()
    }

    override fun searchForWordsInDictionary(pageNo: Int, query: String) {
        val call = apiCLient.searchForWordsInDictionary(pageNo, query)
        val result = call.execute()
    }

}