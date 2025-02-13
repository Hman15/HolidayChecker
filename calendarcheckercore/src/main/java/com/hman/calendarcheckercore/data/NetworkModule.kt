package com.hman.calendarcheckercore.data

import com.hman.calendarcheckercore.data.api.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModule {



    private fun provideRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideCalendarificApi(): CalendarificApi {
        return provideRetrofit( "https://calendarific.com/api/v2/")
            .create(CalendarificApi::class.java)
    }

    fun provideAbstractApi(): AbstractApi {
        return provideRetrofit("https://holidays.abstractapi.com/")
            .create(AbstractApi::class.java)
    }

    fun provideHolidayApi(): HolidayApi {
        return provideRetrofit("https://holidayapi.com")
            .create(HolidayApi::class.java)
    }

}