package com.hman.calendarcheckercore.presentation

import com.hman.calendarcheckercore.data.NetworkModule
import com.hman.calendarcheckercore.data.RepositoryModule
import com.hman.calendarcheckercore.data.model.BaseResponse
import com.hman.calendarcheckercore.domain.*

object HolidayChecker {
    var calendarificApiKey: String = ""
    var abstractApiKey: String = ""
    var holidayApiKey: String = ""

    private lateinit var checkHolidayUseCase: CheckHolidayUseCase

    fun initialize(
        calendarificKey: String = "sQrYuRQSX1Bi1xrmWFQ1W1vEQ0m4DtW2",
        abstractKey: String = "60f06c460e9245589ed865627e740af5",
        holidayKey: String = "6ea999b5-3360-45ae-944d-4b4ab9307946"
    ) {
        calendarificApiKey = calendarificKey
        abstractApiKey = abstractKey
        holidayApiKey = holidayKey

        val networkModule = NetworkModule()
        val calendarificApi = networkModule.provideCalendarificApi()
        val abstractApi = networkModule.provideAbstractApi()
        val holidayApi = networkModule.provideHolidayApi()

        val repositoryModule = RepositoryModule()
        val calendarRepository =
            repositoryModule.createRepository(calendarificApi, abstractApi, holidayApi)
        val validateInputUseCase = ValidateInputUseCase()
        checkHolidayUseCase = CheckHolidayUseCase(calendarRepository, validateInputUseCase)
    }

    suspend fun isHoliday(
        year: Int,
        month: Int,
        day: Int,
        state: Int = CheckType.ANY.value,
        onSuccess: (Boolean) -> Unit,
        onFailure: (List<BaseResponse.Error>) -> Unit
    ) {
        if (!::checkHolidayUseCase.isInitialized) {
            throw IllegalStateException("HolidayCheckerConfig is not initialized. Call initialize() first.")
        }

        checkHolidayUseCase.invoke(
            CheckHolidayParam(
                year = year,
                month = month,
                day = day,
                checkType = state,
                onSuccess = { isHoliday -> onSuccess(isHoliday) },
                onFailure = { errors -> onFailure(errors) }
            )
        )
    }
}