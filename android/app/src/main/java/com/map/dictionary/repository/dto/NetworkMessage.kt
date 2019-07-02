package com.map.dictionary.repository.dto

data class NetworkMessage(val networkState: NetworkState, val message: Int, val loaded: Int = 0)