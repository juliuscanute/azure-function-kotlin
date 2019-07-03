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
import kotlin.math.ceil


class DictionarySearchWordsDataSource(
    private val repository: Repository,
    private val networkState: MutableLiveData<Event<NetworkMessage>>,
    private val query: String
) :
    PositionalDataSource<Meaning>() {

    private var totalPages = 0

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Meaning>) {
        Log.d("DataSource", "loadSize: ${params.loadSize},startPosition: ${params.startPosition}")
        try {
            val startPage = ceil(params.startPosition.toFloat() / PAGE_SIZE.toFloat()).toInt() + 1
            if (totalPages >= startPage) {
                val words = getWordsFromPage(startPage, startPage, query)
                if (words.first.isEmpty()) throw NoDataException()
                callback.onResult(words.first)
            }
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
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Meaning>) {
        Log.d(
            "DataSource",
            "requestedStartPosition: ${params.requestedStartPosition},requestedLoadSize: ${params.requestedLoadSize}"
        )
        try {
            val pair = getWordsFromPage(1, 1, query)
            if (pair.first.isEmpty()) throw NoDataException()
            totalPages = ceil((pair.second).toFloat() / PAGE_SIZE.toFloat()).toInt()
            callback.onResult(
                pair.first,
                0,
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
                    loaded = totalRecords
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