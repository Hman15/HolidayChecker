package com.hman.calendarcheckercore.data.repository

import com.hman.calendarcheckercore.data.api.*
import com.hman.calendarcheckercore.data.model.BaseHoliday
import com.hman.calendarcheckercore.data.model.BaseResponse
import kotlinx.coroutines.flow.*


class CalendarRepositoryImpl(
    private val calendarificApi: CalendarificApi,
    private val abstractApi: AbstractApi,
    private val holidayApi: HolidayApi
) : CalendarRepository {
    override suspend fun getHoliday(
        country: String,
        year: Int,
        month: Int,
        day: Int
    ): Flow<List<BaseResponse<List<BaseHoliday>>>> {
        // Create Flows for each API call
        val calendarificFlow = flow {
            val response = calendarificApi.getHolidays(country, year, month, day)
            emit(CalendarificMapper.toBaseResponse(response))
        }

        val abstractApiFlow = flow {
            val response = abstractApi.getHolidays(country, year, month, day)
            emit(AbstractApiMapper.toBaseResponse(response))
        }

        val holidayApiFlow = flow {
            val response = holidayApi.getHolidays(country, year, month, day)
            emit(HolidayApiMapper.toBaseResponse(response))
        }

        // Combine the Flows to run them concurrently
        return combine(calendarificFlow, abstractApiFlow, holidayApiFlow) { calendarificResult, abstractApiResult, holidayApiResult ->
            listOf(calendarificResult, abstractApiResult, holidayApiResult)
        }
    }
}