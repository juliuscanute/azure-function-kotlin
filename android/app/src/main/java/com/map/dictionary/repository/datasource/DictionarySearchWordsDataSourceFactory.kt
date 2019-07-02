package com.map.dictionary.repository.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.map.dictionary.repository.Event
import com.map.dictionary.repository.Repository
import com.map.dictionary.repository.dto.Meaning
import com.map.dictionary.repository.dto.NetworkMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import java.lang.Exception


class DictionarySearchWordsDataSourceFactory(
    private val repository: Repository,
    private val uiScope: CoroutineScope,
    private val networkState: MutableLiveData<Event<NetworkMessage>>
) :
    DataSource.Factory<Int, Meaning>() {

    val sourceLiveData = MutableLiveData<DictionarySearchWordsDataSource>()
    var query: String = ""

    override fun create(): DataSource<Int, Meaning> {
        val source = DictionarySearchWordsDataSource(repository, uiScope, networkState, query)
        sourceLiveData.postValue(source)
        return source
    }


    fun releaseResouces() {
        try {
            uiScope.cancel()
        } catch (e: Exception) {
            Log.e("UIScope", "Cancelled already")
        }
    }
}