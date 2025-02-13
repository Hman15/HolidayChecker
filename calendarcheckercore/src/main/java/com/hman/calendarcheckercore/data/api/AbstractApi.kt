package com.hman.calendarcheckercore.data.api

import com.google.gson.Gson
import com.hman.calendarcheckercore.data.model.*
import com.hman.calendarcheckercore.presentation.HolidayChecker
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AbstractApi {
    @GET("v1")
    suspend fun getHolidays(
        @Query("api_key") apiKey: String = HolidayChecker.abstractApiKey,
        @Query("country") country: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int,
    ): Response<List<AbstractApiResponse>?>
}

object AbstractApiMapper {
    fun toBaseResponse(response: Response<List<AbstractApiResponse>?>): BaseResponse<List<BaseHoliday>> {
        return if (response.isSuccessful) {
            val holidays = response.body()?.map { holiday ->
                holiday.let {
                    BaseHoliday(
                        name = it.name,
                        date = it.date,
                        type = it.type
                    )
                }
            } ?: emptyList()
            println("abstract success: ${holidays}")
            BaseResponse.Success(holidays)
        } else {
            val errorMessage = try {
                response.errorBody()?.string()?.let { json ->
                    val gson = Gson()
                    val errorResponse = gson.fromJson(json, AbstractApiError::class.java)
                    errorResponse.error?.message ?: "Unknown error"
                }
            } catch (e: Exception) {
                println("Abstract Exception $e")
                e.message
            }

            println("abstract error: $errorMessage")
            BaseResponse.Error(
                code = response.code(),
                message = errorMessage ?: response.message()
            )
        }
    }
}