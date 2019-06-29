package api.data.exception

import java.lang.Exception

class PageNotFoundException(val errorMessage: String) : Exception(errorMessage)