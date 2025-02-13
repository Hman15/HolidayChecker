package com.hman.calendarcheckercore.data.model

data class CalendarificResponse(
    val meta: Meta?,
    val response: Response?
)

data class Meta(
    val code: Int?
)

data class Response(
    val holidays: List<CalendarificHoliday?>?
)

data class CalendarificHoliday(
    val date: CalendarificDate?,
    val description: String?,
    val name: String?,
    val type: List<String?>?
)

data class CalendarificDate(
    val datetime: DateTime?,
    val iso: String?
)

data class DateTime(
    val day: Int?,
    val month: Int?,
    val year: Int?
)