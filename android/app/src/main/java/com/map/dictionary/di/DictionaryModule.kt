package com.map.dictionary.di

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.map.dictionary.controller.MainActivityViewModel
import com.map.dictionary.repository.DictionaryAllWordsDataSourceFactory
import com.map.dictionary.repository.DictionaryRepository
import com.map.dictionary.repository.Event
import com.map.dictionary.repository.Repository
import com.map.dictionary.repository.api.DictionaryApi
import com.map.dictionary.repository.dto.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://dictionary-20190626184020334.azurewebsites.net/"
const val PAGE_SIZE = 20

val dictionaryModule = module {
    single { createApiClient() }
    single { DictionaryRepository(get()) as Repository }
    single { PagedList.Config.Builder().setPageSize(PAGE_SIZE).setEnablePlaceholders(true).build() }
    factory { CoroutineScope(Dispatchers.Main + SupervisorJob()) }
    single { MutableLiveData<Event<NetworkState>>() }
    factory { DictionaryAllWordsDataSourceFactory(get(), get(), get()) }
    viewModel { MainActivityViewModel(get(), get(), get()) }
}


private fun createApiClient(): DictionaryApi {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    return retrofit.create(DictionaryApi::class.java)
}