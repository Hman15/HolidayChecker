package com.hman.calendarcheckercore.data.model

data class HolidayApiResponse(
    val status: Int,  // API response status code
    val requests: Requests?,
    val holidays: List<Holiday>? = emptyList(),
    val error: String? = null,
    val warning: String? = null
)

data class Holiday(
    val name: String,
    val date: String,
    val observed: String,
    val public: Boolean,
    val country: String,
    val uuid: String,
    val weekday: Weekday
)

data class Requests(
    val used: Int,
    val available: Int,
    val resets: String
)

data class Weekday(
    val date: DayInfo,
    val observed: DayInfo
)

data class DayInfo(
    val name: String,
    val numeric: Int
)

