package com.map.dictionary.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.map.dictionary.repository.dto.Meaning
import com.map.dictionary.repository.dto.NetworkMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel


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
        uiScope.cancel()
    }
}