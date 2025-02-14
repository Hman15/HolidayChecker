package com.hman.calendarcheckercore.domain

import com.hman.calendarcheckercore.data.model.BaseHoliday
import com.hman.calendarcheckercore.data.model.BaseResponse
import com.hman.calendarcheckercore.data.repository.CalendarRepository

class CheckHolidayUseCase(
    private val calendarRepository: CalendarRepository,
    private val validateInputUseCase: ValidateInputUseCase
) : UseCase<CheckHolidayParam, Unit>() {
    override suspend fun invoke(param: CheckHolidayParam) {
        param.apply {
            validateInputUseCase.invoke(
                CheckHolidayParam(
                    year, month, day,
                    onSuccess = {
                        performHolidayCheck(param)
                    },
                    onFailure = {
                        onFailure.invoke(it)
                    }
                )
            )
        }
    }

    private suspend fun performHolidayCheck(param: CheckHolidayParam) {
        calendarRepository.getHoliday(param.country, param.year, param.month, param.day)
            .collect { result ->
                val successfulResults = result.filterIsInstance<BaseResponse.Success<List<BaseHoliday>>>()
                val errorResults = result.filterIsInstance<BaseResponse.Error>()

                val isHoliday = when (param.checkType) {
                    CheckType.ANY.value -> {
                        successfulResults.any { it.data.isNotEmpty() }
                    }

                    CheckType.ALL.value -> {
                        successfulResults.all { it.data.isNotEmpty() }
                    }

                    CheckType.CONSENSUS.value -> {
                        val totalApis = result.size
                        val apisWithHoliday = successfulResults.count { it.data.isNotEmpty() }
                        apisWithHoliday > totalApis / 2
                    }

                    else -> false
                }

                if (successfulResults.isNotEmpty()) {
                    param.onSuccess.invoke(isHoliday)
                }
                if (errorResults.isNotEmpty()) {
                    param.onFailure.invoke(errorResults)
                }
            }
    }
}

data class CheckHolidayParam(
    val year: Int,
    val month: Int,
    val day: Int,
    val checkType: Int = CheckType.ANY.value,
    val country: String = "VN",
    val onSuccess: suspend (Boolean) -> Unit,
    val onFailure: (List<BaseResponse.Error>) -> Unit
)

enum class CheckType(val value: Int) {
    ANY(0), ALL(1), CONSENSUS(2)
}