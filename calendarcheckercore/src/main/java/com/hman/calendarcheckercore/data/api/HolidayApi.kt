package com.hman.calendarcheckercore.data.api

import com.google.gson.Gson
import com.hman.calendarcheckercore.data.model.*
import com.hman.calendarcheckercore.presentation.HolidayChecker
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HolidayApi {
    @GET("/v1/holidays")
    suspend fun getHolidays(
        @Query("key") apiKey: String = HolidayChecker.holidayApiKey,
        @Query("country") country: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int,
    ): Response<HolidayApiResponse>
}

object HolidayApiMapper {
    fun toBaseResponse(response: Response<HolidayApiResponse>): BaseResponse<List<BaseHoliday>> {
        return if (response.isSuccessful) {
            val holidays = response.body()?.holidays?.map { holiday ->
                BaseHoliday(
                    name = holiday.name,
                    date = holiday.date,
                    type = if (holiday.public) "Public" else "Private"
                )
            } ?: emptyList()
            println("holiday success: $holidays")
            BaseResponse.Success(holidays)
        } else {
            val errorMessage = try {
                response.errorBody()?.string()?.let { json ->
                    val gson = Gson()
                    val errorResponse = gson.fromJson(json, HolidayApiResponse::class.java)
                    errorResponse.error ?: "Unknown error"
                }
            } catch (e: Exception) {
                e.message
            }

            println("holiday error: $errorMessage")
            BaseResponse.Error(
                code = response.code(),
                message = errorMessage ?: response.message()
            )
        }
    }
}