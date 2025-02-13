package com.hman.calendarcheckercore.data.repository

import com.hman.calendarcheckercore.data.api.*
import com.hman.calendarcheckercore.data.model.BaseHoliday
import com.hman.calendarcheckercore.data.model.BaseResponse


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
    ): List<BaseResponse<List<BaseHoliday>>> {
        val calendarificResponse =
            calendarificApi.getHolidays(country = country, year = year, month = month, day = day)
        val abstractApiResponse =
            abstractApi.getHolidays(country = country, year = year, month = month, day = day)
        val holidayApiResponse =
            holidayApi.getHolidays(country = country, year = year, month = month, day = day)

        val calendarificResult = CalendarificMapper.toBaseResponse(calendarificResponse)
        val abstractApiResult = AbstractApiMapper.toBaseResponse(abstractApiResponse)
        val holidayApiResult = HolidayApiMapper.toBaseResponse(holidayApiResponse)

        val allResults = listOf(calendarificResult, abstractApiResult, holidayApiResult)

        return allResults
    }
}