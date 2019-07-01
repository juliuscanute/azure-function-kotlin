package com.map.dictionary.repository

import androidx.paging.DataSource
import com.map.dictionary.repository.dto.Meaning
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

class DictionaryAllWordsDataSourceFactory(private val repository: Repository,private val uiScope: CoroutineScope) :
    DataSource.Factory<Int, Meaning>() {
    override fun create(): DataSource<Int, Meaning> {
        return DictionaryAllWordsDataSource(repository,uiScope)
    }

    fun releaseResouces(){
        uiScope.cancel()
    }
}