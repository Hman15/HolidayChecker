package com.hman.calendarcheckercore.data.api

import com.google.gson.Gson
import com.hman.calendarcheckercore.data.model.*
import com.hman.calendarcheckercore.presentation.HolidayChecker
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CalendarificApi {
    @GET("holidays")
    suspend fun getHolidays(
        @Query("country") country: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int,
        @Query("api_key") apiKey: String = HolidayChecker.calendarificApiKey
    ): Response<CalendarificResponse?>
}

object CalendarificMapper {
    fun toBaseResponse(response: Response<CalendarificResponse?>): BaseResponse<List<BaseHoliday>> {
        return if (response.isSuccessful) {
            val holidays = response.body()?.response?.holidays?.mapNotNull { holiday ->
                holiday?.let {
                    BaseHoliday(
                        name = it.name,
                        date = it.date?.iso,
                        type = it.type?.joinToString(", ")
                    )
                }
            } ?: emptyList()
            println("calendarific success: $holidays")
            BaseResponse.Success(holidays)
        } else {
            val errorMessage = try {
                response.errorBody()?.string()?.let { json ->
                    val gson = Gson()
                    val errorResponse = gson.fromJson(json, CalendarificError::class.java)
                    errorResponse.meta?.errorDetail ?: "Unknown error"
                }
            } catch (e: Exception) {
                println("Calendarific Exception $e")
                e.message
            }

            println("Calendarific error: $errorMessage")
            BaseResponse.Error(
                code = response.code(),
                message = errorMessage ?: response.message()
            )
        }
    }
}