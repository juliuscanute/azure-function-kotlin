package com.map.dictionary.repository

import com.map.dictionary.repository.dto.Dictionary
import org.koin.core.KoinComponent

interface Repository : KoinComponent {
    fun getNumberOfPagesInDictionary()
    fun getPageInDictionary(pageNo: Int): Dictionary
    fun searchForWordsInDictionary(pageNo: Int, query: String)
}