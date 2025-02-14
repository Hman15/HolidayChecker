package com.hman.calendarcheckercore

import com.hman.calendarcheckercore.data.model.BaseResponse
import com.hman.calendarcheckercore.domain.*
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ValidateInputUseCaseTest {
    private val validateInputUseCase = ValidateInputUseCase()

    @Test
    fun testFieldsAllValid() = runTest {
        val mockCallback: (Boolean) -> Unit = mockk(relaxed = true)
        val param = CheckHolidayParam(
            year = 2023,
            month = 1,
            day = 1,
            checkType = CheckType.ANY.value,
            onSuccess = mockCallback,
            onFailure = { }
        )

        validateInputUseCase.invoke(param)

        coVerify {
            mockCallback.invoke(true)
        }
    }

    @Test
    fun testInvalidYear() = runTest {
        val mockCallback: (List<BaseResponse.Error>) -> Unit = mockk(relaxed = true)
        val param = CheckHolidayParam(
            year = 3001,
            month = 1,
            day = 1,
            checkType = CheckType.ANY.value,
            onSuccess = { },
            onFailure = mockCallback
        )

        validateInputUseCase.invoke(param)

        coVerify {
            mockCallback.invoke(
                listOf(
                    BaseResponse.Error(
                        code = 100,
                        message = "Year must be between 0 and 3000"
                    )
                )
            )
        }
    }

    @Test
    fun testInvalidMonth() = runTest {
        val mockCallback: (List<BaseResponse.Error>) -> Unit = mockk(relaxed = true)
        val param = CheckHolidayParam(
            year = 2025,
            month = 13,
            day = 1,
            checkType = CheckType.ANY.value,
            onSuccess = { },
            onFailure = mockCallback
        )

        validateInputUseCase.invoke(param)

        coVerify {
            mockCallback.invoke(
                listOf(
                    BaseResponse.Error(code = 101, message = "Month must be between 1 and 12")
                )
            )
        }
    }

    @Test
    fun testInvalidDay() = runTest {
        val mockCallback: (List<BaseResponse.Error>) -> Unit = mockk(relaxed = true)
        val param = CheckHolidayParam(
            year = 2025,
            month = 9,
            day = 31,
            checkType = CheckType.ANY.value,
            onSuccess = { },
            onFailure = mockCallback
        )

        validateInputUseCase.invoke(param)

        coVerify {
            mockCallback.invoke(
                listOf(
                    BaseResponse.Error(
                        code = 102,
                        message = "Invalid day: For month ${param.month} and year ${param.year}, day must be between 1 and 30."
                    )
                )
            )
        }
    }

    @Test
    fun testInvalidDayLeapYear() = runTest {
        val mockCallback: (List<BaseResponse.Error>) -> Unit = mockk(relaxed = true)
        val param = CheckHolidayParam(
            year = 2025,
            month = 2,
            day = 29,
            checkType = CheckType.ANY.value,
            onSuccess = { },
            onFailure = mockCallback
        )

        validateInputUseCase.invoke(param)

        coVerify {
            mockCallback.invoke(
                listOf(
                    BaseResponse.Error(
                        code = 102,
                        message = "Invalid day: For month ${param.month} and year ${param.year}, day must be between 1 and 28."
                    )
                )
            )
        }
    }

    @Test
    fun testInvalidCheckType() = runTest {
        val mockCallback: (List<BaseResponse.Error>) -> Unit = mockk(relaxed = true)
        val param = CheckHolidayParam(
            year = 2025,
            month = 1,
            day = 1,
            checkType = 3,
            onSuccess = { },
            onFailure = mockCallback
        )

        validateInputUseCase.invoke(param)

        coVerify {
            mockCallback.invoke(
                listOf(
                    BaseResponse.Error(
                        code = 103,
                        message = "Invalid check type: Check type must be one of ANY(0), ALL(1), CONSENSUS(2)."
                    )
                )
            )
        }
    }
}