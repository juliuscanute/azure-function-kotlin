package com.map.dictionary.controller


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.map.dictionary.repository.DictionaryAllWordsDataSourceFactory
import com.map.dictionary.repository.Event
import com.map.dictionary.repository.dto.Meaning
import com.map.dictionary.repository.dto.NetworkMessage

class MainActivityViewModel(
    config: PagedList.Config,
    private val dataSourceFactory: DictionaryAllWordsDataSourceFactory,
    val networkState: MutableLiveData<Event<NetworkMessage>>
) :
    ViewModel() {
    val words: LiveData<PagedList<Meaning>> = LivePagedListBuilder(dataSourceFactory, config).build()

    override fun onCleared() {
        super.onCleared()
        dataSourceFactory.releaseResouces()
    }
}