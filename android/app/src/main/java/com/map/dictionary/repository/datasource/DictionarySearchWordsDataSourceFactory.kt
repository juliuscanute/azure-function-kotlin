package com.map.dictionary.repository.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.map.dictionary.repository.Event
import com.map.dictionary.repository.Repository
import com.map.dictionary.repository.dto.Meaning
import com.map.dictionary.repository.dto.NetworkMessage


class DictionarySearchWordsDataSourceFactory(
    private val repository: Repository,
    private val networkState: MutableLiveData<Event<NetworkMessage>>
) :
    DataSource.Factory<Int, Meaning>() {

    val sourceLiveData = MutableLiveData<DictionarySearchWordsDataSource>()
    var query: String = ""

    override fun create(): DataSource<Int, Meaning> {
        val source = DictionarySearchWordsDataSource(repository, networkState, query)
        sourceLiveData.postValue(source)
        return source
    }
}