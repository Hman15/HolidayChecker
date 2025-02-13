package com.hman.calendarcheckercore.data

import com.hman.calendarcheckercore.data.api.*
import com.hman.calendarcheckercore.data.repository.CalendarRepository
import com.hman.calendarcheckercore.data.repository.CalendarRepositoryImpl

class RepositoryModule {
    fun createRepository(
        calendarificApi: CalendarificApi,
        abstractApi: AbstractApi,
        holidayApi: HolidayApi
    ): CalendarRepository {
        return CalendarRepositoryImpl(calendarificApi, abstractApi, holidayApi)
    }
}