package com.hman.calendarcheckercore.data.repository

import com.hman.calendarcheckercore.data.model.*
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {
    suspend fun getHoliday(
        country: String,
        year: Int,
        month: Int,
        day: Int
    ): Flow<List<BaseResponse<List<BaseHoliday>>>>
}