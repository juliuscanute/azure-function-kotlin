package com.map.dictionary.controller


import androidx.lifecycle.ViewModel
import com.map.dictionary.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext

class MainActivityViewModel(repository: Repository) : ViewModel() {
    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }


    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
        }
    }

    private suspend fun getData(tramStop: String) = withContext(Dispatchers.Default) {

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}