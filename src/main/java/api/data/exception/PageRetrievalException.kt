package api.data.exception

import java.lang.Exception

class PageRetrievalException(val errorMessage: String) : Exception(errorMessage)