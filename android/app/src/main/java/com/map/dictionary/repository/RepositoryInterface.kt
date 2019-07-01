package com.map.dictionary.repository

import com.map.dictionary.repository.dto.Dictionary
import com.map.dictionary.repository.dto.Page
import com.map.dictionary.repository.dto.SearchResult
import org.koin.core.KoinComponent

interface RepositoryInterface : KoinComponent {
    fun getNumberOfPagesInDictionary(): Page
    fun getPageInDictionary(pageNo: Int): Dictionary
    fun searchForWordsInDictionary(pageNo: Int, query: String): SearchResult
}