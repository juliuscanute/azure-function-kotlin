package com.map.dictionary.di

import com.map.dictionary.controller.MainActivityViewModel
import com.map.dictionary.repository.DictionaryRepository
import com.map.dictionary.repository.Repository
import com.map.dictionary.repository.api.DictionaryApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://dictionary-20190626184020334.azurewebsites.net/api"

val dictionaryModule = module {
    single { createApiClient() }
    single { DictionaryRepository(get()) as Repository}
    viewModel { MainActivityViewModel(get()) }
}


private fun createApiClient(): DictionaryApi {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    return retrofit.create(DictionaryApi::class.java)
}