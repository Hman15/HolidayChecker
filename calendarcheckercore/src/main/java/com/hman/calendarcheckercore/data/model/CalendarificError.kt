package com.hman.calendarcheckercore.data.model

import com.google.gson.annotations.SerializedName

data class CalendarificError(
    val meta: ErrorMeta?,
    val response: List<Any?>?
)

data class ErrorMeta(
    val code: Int?,
    @SerializedName("error_type")
    val errorType: String?,
    @SerializedName("error_detail")
    val errorDetail: String?
)