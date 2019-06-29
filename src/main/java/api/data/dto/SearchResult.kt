package api.data.dto

data class SearchResult(val startPage: Int = 0, val endPage: Int = 0, val totalRecords: Int = 0, val currentPage: Int = 0, val words: List<Meaning>)