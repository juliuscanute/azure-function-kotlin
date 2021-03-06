package com.map.dictionary.repository.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.map.dictionary.R
import com.map.dictionary.di.PAGE_SIZE
import com.map.dictionary.repository.Event
import com.map.dictionary.repository.Repository
import com.map.dictionary.repository.dto.*
import com.map.dictionary.repository.exception.FetchException
import com.map.dictionary.repository.exception.NoDataException
import kotlin.math.min

class DictionaryAllWordsDataSource(
    private val repository: Repository,
    private val networkState: MutableLiveData<Event<NetworkMessage>>
) :
    PositionalDataSource<Meaning>() {

    private var totalPages = 0

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Meaning>) {
        Log.d("DataSource", "loadSize: ${params.loadSize},startPosition: ${params.startPosition}")
        try {
            val startPage = (params.startPosition / PAGE_SIZE) + 1
            if (totalPages >= startPage) {
                val words = getWordsFromPage(startPage, startPage)
                if (words.isEmpty()) throw NoDataException()
                callback.onResult(words)
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
            val total = getTotalPages()
            val endPage = min((params.requestedLoadSize / PAGE_SIZE), 3)
            val startPage = 1
            val words = getWordsFromPage(startPage, endPage)
            if (words.isEmpty()) throw NoDataException()
            callback.onResult(words, 0, (total.end * PAGE_SIZE))
            totalPages = total.end
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

    fun getWordsFromPage(startPage: Int, endPage: Int): MutableList<Meaning> {
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

    private fun getWordsFromPage(pageNo: Int): Dictionary {
        networkState.postValue(
            Event(
                NetworkMessage(
                    networkState = NetworkState.NETWORK_FETCH_START,
                    message = R.string.load_start
                )
            )
        )
        return repository.getPageInDictionary(pageNo)
    }

    private fun getTotalPages(): Page {
        networkState.postValue(
            Event(
                NetworkMessage(
                    networkState = NetworkState.NETWORK_FETCH_START,
                    message = R.string.load_start
                )
            )
        )
        return repository.getNumberOfPagesInDictionary()
    }
}