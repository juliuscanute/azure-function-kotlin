package com.map.dictionary.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.map.dictionary.R
import com.map.dictionary.di.PAGE_SIZE
import com.map.dictionary.repository.dto.Meaning
import com.map.dictionary.repository.dto.NetworkMessage
import com.map.dictionary.repository.dto.NetworkState
import com.map.dictionary.repository.exception.FetchException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.min

class DictionaryAllWordsDataSource(
    private val repository: Repository,
    private val uiScope: CoroutineScope,
    private val networkState: MutableLiveData<Event<NetworkMessage>>
) :
    PositionalDataSource<Meaning>() {

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Meaning>) {
        Log.d("DataSource", "loadSize: ${params.loadSize},startPosition: ${params.startPosition}")
        uiScope.launch {
            try {
                val startPage = params.startPosition / PAGE_SIZE
                val endPage = (startPage + params.loadSize / PAGE_SIZE) - 1
                val words = getWordsFromPage(startPage, endPage)
                callback.onResult(words)
            } catch (e: FetchException) {
                networkState.postValue(
                    Event(
                        NetworkMessage(
                            networkState = NetworkState.NETWORK_FETCH_ERROR,
                            message = R.string.load_complete_error
                        )
                    )
                )
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Meaning>) {
        Log.d(
            "DataSource",
            "requestedStartPosition: ${params.requestedStartPosition},requestedLoadSize: ${params.requestedLoadSize}"
        )
        uiScope.launch {
            try {
                val total = getTotalPages()
                val endPage = min((params.requestedLoadSize / PAGE_SIZE), 3)
                val startPage = 1
                val words = getWordsFromPage(startPage, endPage)
                callback.onResult(words, params.requestedStartPosition, (total.end * PAGE_SIZE))
            } catch (e: FetchException) {
                networkState.postValue(
                    Event(
                        NetworkMessage(
                            networkState = NetworkState.NETWORK_FETCH_ERROR,
                            message = R.string.load_complete_error
                        )
                    )
                )
            }
        }
    }

    suspend fun getWordsFromPage(startPage: Int, endPage: Int): MutableList<Meaning> {
        val words = mutableListOf<Meaning>()
        for (index in startPage..endPage) {
            val dictionary = getWordsFromPage(index)
            dictionary.words.forEach { words.add(it) }
        }
        networkState.postValue(
            Event(
                NetworkMessage(
                    networkState = NetworkState.NETWORK_FETCH_SUCCESS,
                    message = R.string.load_complete_success,
                    loaded = words.size
                )
            )
        )
        return words
    }

    private suspend fun getWordsFromPage(pageNo: Int) = withContext(Dispatchers.Default) {
        networkState.postValue(
            Event(
                NetworkMessage(
                    networkState = NetworkState.NETWORK_FETCH_START,
                    message = R.string.load_start
                )
            )
        )
        repository.getPageInDictionary(pageNo)
    }

    private suspend fun getTotalPages() = withContext(Dispatchers.Default) {
        networkState.postValue(
            Event(
                NetworkMessage(
                    networkState = NetworkState.NETWORK_FETCH_START,
                    message = R.string.load_start
                )
            )
        )
        repository.getNumberOfPagesInDictionary()
    }
}