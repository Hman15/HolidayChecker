package com.hman.calendarcheckercore.data.model

data class AbstractApiError(
    val error: Error?
)

data class Error(
    val code: String?,
    val details: Any?,
    val message: String?
)