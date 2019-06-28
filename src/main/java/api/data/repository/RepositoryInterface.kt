package api.data.repository

import api.data.Page
import org.koin.core.KoinComponent


interface RepositoryInterface : KoinComponent {
    fun getNumberOfPagesInDictionary() : Page
}