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


class DictionaryAllWordsDataSourceFactory(
    private val repository: Repository,
    private val uiScope: CoroutineScope,
    private val networkState: MutableLiveData<Event<NetworkMessage>>
) :
    DataSource.Factory<Int, Meaning>() {
    override fun create(): DataSource<Int, Meaning> {
        return DictionaryAllWordsDataSource(repository, uiScope, networkState)
    }

    fun releaseResouces() {
        try {
            uiScope.cancel()
        }catch (e: Exception){
            Log.e("UIScope","Cancelled already")
        }
    }
}