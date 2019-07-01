package com.map.dictionary.repository

import android.util.Log
import androidx.paging.PositionalDataSource
import com.map.dictionary.di.PAGE_SIZE
import com.map.dictionary.repository.dto.Meaning
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DictionaryAllWordsDataSource(private val repository: Repository, private val uiScope: CoroutineScope) :
    PositionalDataSource<Meaning>() {

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Meaning>) {
        Log.e("JULIUS", "loadSize: ${params.loadSize},startPosition: ${params.startPosition}")

    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Meaning>) {
        Log.e(
            "JULIUS",
            "requestedStartPosition: ${params.requestedStartPosition},requestedLoadSize: ${params.requestedLoadSize}"
        )
        uiScope.launch {
            val words = getWordsFromPage(params)
            callback.onResult(words, params.requestedStartPosition, words.size)
        }
    }

    suspend fun getWordsFromPage(params: LoadInitialParams): MutableList<Meaning> {
        val endPage = params.requestedLoadSize / PAGE_SIZE
        val startPage = 1
        val words = mutableListOf<Meaning>()
        for (index in startPage..endPage) {
            val dictionary = getWordsFromPage(index)
            dictionary.words.forEach { words.add(it) }
        }
        return words
    }

    private suspend fun getWordsFromPage(pageNo: Int) = withContext(Dispatchers.Default) {
        repository.getPageInDictionary(pageNo)
    }

}