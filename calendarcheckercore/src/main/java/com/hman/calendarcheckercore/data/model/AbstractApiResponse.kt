package com.hman.calendarcheckercore.data.model

data class AbstractApiResponse(
    val country: String?,
    val date: String?,
    val date_day: String?,
    val date_month: String?,
    val date_year: String?,
    val description: String?,
    val language: String?,
    val location: String?,
    val name: String?,
    val name_local: String?,
    val type: String?,
    val week_day: String?
)