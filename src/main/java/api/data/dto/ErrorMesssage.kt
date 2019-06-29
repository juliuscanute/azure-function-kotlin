package api.data.dto

import api.data.exception.ErrorType
import com.google.gson.annotations.Expose

data class ErrorMesssage(@Expose var path: String="", @Expose val message: String="", val type: ErrorType)