package com.map.dictionary.repository

import org.koin.core.KoinComponent

interface Repository : KoinComponent {
    fun getNumberOfPagesInDictionary()
    fun getPageInDictionary(pageNo: Int)
    fun searchForWordsInDictionary(pageNo: Int, query: String)
}