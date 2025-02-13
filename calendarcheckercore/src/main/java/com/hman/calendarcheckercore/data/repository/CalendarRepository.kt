package com.hman.calendarcheckercore.data.repository

import com.hman.calendarcheckercore.data.model.*

interface CalendarRepository {
    suspend fun getHoliday(
        country: String,
        year: Int,
        month: Int,
        day: Int
    ): List<BaseResponse<List<BaseHoliday>>>
}