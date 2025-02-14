package com.hman.calendarcheckercore.data.model

sealed class BaseResponse<out T> {
    data class Success<out T>(val data: T) : BaseResponse<T>()
    data class Error(val code: Int, val message: String) : BaseResponse<Nothing>()
}