package com.map.dictionary.controller


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.map.dictionary.repository.datasource.DictionaryAllWordsDataSourceFactory
import com.map.dictionary.repository.Event
import com.map.dictionary.repository.datasource.DictionarySearchWordsDataSourceFactory
import com.map.dictionary.repository.dto.Meaning
import com.map.dictionary.repository.dto.NetworkMessage

private const val ALL = "all"
private const val SEARCH = "search"

class MainActivityViewModel(
    private val config: PagedList.Config,
    private val dataSourceFactory: DictionaryAllWordsDataSourceFactory,
    private val searchDataSourceFactory: DictionarySearchWordsDataSourceFactory,
    val networkState: MutableLiveData<Event<NetworkMessage>>
) :
    ViewModel() {

    private val mapOfLiveData: HashMap<String, LiveData<PagedList<Meaning>>> = HashMap()
    val words: MediatorLiveData<PagedList<Meaning>> = MediatorLiveData()
    var searchShown: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    var searchQuery: MutableLiveData<String> = MutableLiveData()

    init {
        addAllWordsAndRemoveSearch()
    }

    fun getSearchState() = searchShown.value ?: false

    fun searchStart() {
        searchShown.postValue(true)
    }

    fun searchEnd() {
        searchShown.postValue(false)
        searchQuery.postValue("")
        addAllWordsAndRemoveSearch()
    }

    fun searchWord(query: String) {
        searchQuery.postValue(query)
        searchDataSourceFactory.query = query
        addSearchAndRemoveAllWords()
    }

    fun invalidateDataSource() {
        words.value?.dataSource?.invalidate()
    }

    private fun addAllWordsAndRemoveSearch() {
        removeSearchObserver()
        if (!mapOfLiveData.containsKey(ALL)) {
            mapOfLiveData[ALL] = LivePagedListBuilder(dataSourceFactory, config).build()
            words.addSource(mapOfLiveData[ALL]!!) {
                words.value = it
            }
        }
    }


    private fun addSearchAndRemoveAllWords() {
        removeAllWordsObserver()
        if (!mapOfLiveData.containsKey(SEARCH)) {
            mapOfLiveData[SEARCH] = LivePagedListBuilder(searchDataSourceFactory, config).build()
            words.addSource(mapOfLiveData[SEARCH]!!) {
                words.value = it
            }
        }
    }

    private fun removeSearchObserver() {
        if (mapOfLiveData.containsKey(SEARCH)) {
            words.removeSource(mapOfLiveData[SEARCH]!!)
            mapOfLiveData.remove(SEARCH)
        }
    }

    private fun removeAllWordsObserver() {
        if (mapOfLiveData.containsKey(ALL)) {
            words.removeSource(mapOfLiveData[ALL]!!)
            mapOfLiveData.remove(ALL)
        }
    }

}