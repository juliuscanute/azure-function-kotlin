package com.map.dictionary.repository.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.map.dictionary.R
import com.map.dictionary.di.PAGE_SIZE
import com.map.dictionary.repository.Event
import com.map.dictionary.repository.Repository
import com.map.dictionary.repository.dto.Meaning
import com.map.dictionary.repository.dto.NetworkMessage
import com.map.dictionary.repository.dto.NetworkState
import com.map.dictionary.repository.dto.SearchResult
import com.map.dictionary.repository.exception.FetchException
import com.map.dictionary.repository.exception.NoDataException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.min

class DictionarySearchWordsDataSource(
    private val repository: Repository,
    private val uiScope: CoroutineScope,
    private val networkState: MutableLiveData<Event<NetworkMessage>>,
    private val query: String
) :
    PositionalDataSource<Meaning>() {

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Meaning>) {
        Log.d("DataSource", "loadSize: ${params.loadSize},startPosition: ${params.startPosition}")
//        uiScope.launch {
            try {
                val startPage = params.startPosition / PAGE_SIZE
                val endPage = (startPage + params.loadSize / PAGE_SIZE) - 1
                val words = getWordsFromPage(startPage, endPage, query)
                if (words.first.isEmpty()) throw NoDataException()
                callback.onResult(words.first)
            } catch (e: FetchException) {
                networkState.postValue(
                    Event(
                        NetworkMessage(
                            networkState = NetworkState.NETWORK_FETCH_ERROR,
                            message = R.string.load_complete_error
                        )
                    )
                )
            } catch (e: NoDataException) {
                networkState.postValue(
                    Event(
                        NetworkMessage(
                            networkState = NetworkState.NETWORK_NO_DATA,
                            message = R.string.no_data_found
                        )
                    )
                )
            }
//        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Meaning>) {
        Log.d(
            "DataSource",
            "requestedStartPosition: ${params.requestedStartPosition},requestedLoadSize: ${params.requestedLoadSize}"
        )
//        uiScope.launch {
            try {
                val pair = getWordsFromPage(1, 1, query)
                val adjustSize = min(params.requestedLoadSize, pair.first.size)
                val subList = pair.first.subList(0, adjustSize)
                if (subList.isEmpty()) throw NoDataException()
                callback.onResult(
                    subList,
                    params.requestedStartPosition,
                    pair.second
                )
            } catch (e: FetchException) {
                networkState.postValue(
                    Event(
                        NetworkMessage(
                            networkState = NetworkState.NETWORK_FETCH_ERROR,
                            message = R.string.load_complete_error
                        )
                    )
                )
            } catch (e: NoDataException) {
                networkState.postValue(
                    Event(
                        NetworkMessage(
                            networkState = NetworkState.NETWORK_NO_DATA,
                            message = R.string.no_data_found
                        )
                    )
                )
            }
//        }
    }

    fun getWordsFromPage(startPage: Int, endPage: Int, query: String): Pair<MutableList<Meaning>, Int> {
        val words = mutableListOf<Meaning>()
        var totalRecords = 0
        for (index in startPage..endPage) {
            val dictionary = getWordsFromPage(index, query)
            dictionary.words.forEach { words.add(it) }
            totalRecords = dictionary.totalRecords
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

        return Pair(words, totalRecords)
    }

    private fun getWordsFromPage(pageNo: Int, query: String): SearchResult {
        networkState.postValue(
            Event(
                NetworkMessage(
                    networkState = NetworkState.NETWORK_FETCH_START,
                    message = R.string.load_start
                )
            )
        )
        return repository.searchForWordsInDictionary(pageNo, query)
    }
}