package com.map.dictionary.repository.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.map.dictionary.repository.Event
import com.map.dictionary.repository.Repository
import com.map.dictionary.repository.dto.Meaning
import com.map.dictionary.repository.dto.NetworkMessage

class DictionaryAllWordsDataSourceFactory(
    private val repository: Repository,
    private val networkState: MutableLiveData<Event<NetworkMessage>>
) :
    DataSource.Factory<Int, Meaning>() {
    override fun create(): DataSource<Int, Meaning> {
        return DictionaryAllWordsDataSource(repository, networkState)
    }
}