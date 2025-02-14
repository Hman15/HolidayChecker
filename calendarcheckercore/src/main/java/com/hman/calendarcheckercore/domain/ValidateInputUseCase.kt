package com.hman.calendarcheckercore.domain

import com.hman.calendarcheckercore.data.model.BaseResponse

class ValidateInputUseCase : UseCase<CheckHolidayParam, Unit>() {
    override suspend fun invoke(param: CheckHolidayParam) {
        fun isLeapYear(year: Int): Boolean =
            (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

        param.apply {
            val errorList = mutableListOf<BaseResponse.Error>()

            // Validate the year.
            if (year !in 0..3000) {
                errorList.add(BaseResponse.Error(code = 100, message = "Year must be between 0 and 3000"))
                onFailure.invoke(errorList)
                return
            }

            // Validate the month.
            if (month !in 1..12) {
                errorList.add(BaseResponse.Error(code = 101, message = "Month must be between 1 and 12"))
                onFailure.invoke(errorList)
                return
            }

            val maxDay: Int = when (month) {
                1, 3, 5, 7, 8, 10, 12 -> 31
                4, 6, 9, 11 -> 30
                2 -> if (isLeapYear(year)) 29 else 28
                else -> 31 // Fallback; should never occur as month is validated.
            }

            // Validate the day.
            if (day !in 1..maxDay) {
                errorList.add(
                    BaseResponse.Error(
                        code = 102,
                        message = "Invalid day: For month $month and year $year, day must be between 1 and $maxDay."
                    )
                )
                onFailure.invoke(errorList)
                return
            }

            // Validate input type
            if (checkType !in 0..2) {
                errorList.add(
                    BaseResponse.Error(
                        code = 103,
                        message = "Invalid check type: Check type must be one of ANY(0), ALL(1), CONSENSUS(2)."
                    )
                )
                onFailure.invoke(errorList)
                return
            }

            // If all validations pass, invoke onSuccess.
            onSuccess.invoke(true)
        }
    }
}