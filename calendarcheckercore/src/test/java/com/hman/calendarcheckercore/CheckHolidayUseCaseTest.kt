package com.hman.calendarcheckercore

import com.hman.calendarcheckercore.data.model.BaseHoliday
import com.hman.calendarcheckercore.data.model.BaseResponse
import com.hman.calendarcheckercore.data.repository.CalendarRepository
import com.hman.calendarcheckercore.domain.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CheckHolidayUseCaseTest {
    private lateinit var checkHolidayUseCase: CheckHolidayUseCase
    private var validateInputUseCase: ValidateInputUseCase = ValidateInputUseCase()
    private var calendarRepository: CalendarRepository = mockk()

    @Before
    fun setup() {
        checkHolidayUseCase = CheckHolidayUseCase(calendarRepository, validateInputUseCase)
    }

    @Test
    fun testCheckHolidayUseCaseSuccessAnyHoliday() = runTest {
        val inputParam = CheckHolidayParam(
            year = 2023,
            month = 1,
            day = 1,
            checkType = CheckType.ANY.value,
            onSuccess = {
                assertTrue(it)
            },
            onFailure = {}
        )

        val flow = flow {
            val mockResult =
                listOf(
                    BaseResponse.Success(emptyList()),
                    BaseResponse.Success(emptyList()),
                    BaseResponse.Success(listOf(BaseHoliday("Test Holiday", "2023-01-01", "Public")))
                )
            emit(mockResult)
        }

        coEvery { calendarRepository.getHoliday(any(), any(), any(), any()) } returns flow

        checkHolidayUseCase.invoke(param = inputParam)
    }

    @Test
    fun testCheckHolidaySuccessNoHoliday() = runTest {
        val inputParam = CheckHolidayParam(
            year = 2023,
            month = 2,
            day = 11,
            checkType = CheckType.ANY.value,
            onSuccess = {
                assertFalse(it)
            },
            onFailure = {}
        )

        val flow = flow {
            val mockResult =
                listOf(BaseResponse.Success(emptyList<BaseHoliday>()))
            emit(mockResult)
        }

        coEvery { calendarRepository.getHoliday(any(), any(), any(), any()) } returns flow

        checkHolidayUseCase.invoke(param = inputParam)
    }

    @Test
    fun testCheckHolidayFail() = runTest {
        val inputParam = CheckHolidayParam(
            year = 2023,
            month = 2,
            day = 11,
            checkType = CheckType.ANY.value,
            onSuccess = {
            },
            onFailure = {
                assertTrue(it.isNotEmpty())
            }
        )

        val flow = flow {
            val mockResult =
                listOf(BaseResponse.Error(code = 404, message = "Error message"))
            emit(mockResult)
        }

        coEvery { calendarRepository.getHoliday(any(), any(), any(), any()) } returns flow

        checkHolidayUseCase.invoke(param = inputParam)
    }

    @Test
    fun testCheckHolidayFailNotAllHoliday() = runTest {
        val inputParam = CheckHolidayParam(
            year = 2023,
            month = 2,
            day = 11,
            checkType = CheckType.ALL.value,
            onSuccess = {
                assertFalse(it)
            },
            onFailure = {}
        )

        val flow = flow {
            val mockResult =
                listOf(
                    BaseResponse.Success(emptyList()),
                    BaseResponse.Success(listOf(BaseHoliday("Test Holiday", "2023-01-01", "Public"))),
                    BaseResponse.Success(emptyList())
                )
            emit(mockResult)
        }

        coEvery { calendarRepository.getHoliday(any(), any(), any(), any()) } returns flow

        checkHolidayUseCase.invoke(param = inputParam)
    }

    @Test
    fun testCheckHolidaySuccessAllHoliday() = runTest {
        val inputParam = CheckHolidayParam(
            year = 2023,
            month = 2,
            day = 11,
            checkType = CheckType.ALL.value,
            onSuccess = {
                assertTrue(it)
            },
            onFailure = {}
        )

        val flow = flow {
            val mockResult =
                listOf(
                    BaseResponse.Success(listOf(BaseHoliday("Test Holiday", "2023-01-01", "Public"))),
                    BaseResponse.Success(listOf(BaseHoliday("Test Holiday", "2023-01-01", "Public"))),
                    BaseResponse.Success(listOf(BaseHoliday("Test Holiday", "2023-01-01", "Public")))
                )
            emit(mockResult)
        }

        coEvery { calendarRepository.getHoliday(any(), any(), any(), any()) } returns flow

        checkHolidayUseCase.invoke(param = inputParam)
    }

    @Test
    fun testCheckHolidaySuccessConsensus() = runTest {
        val inputParam = CheckHolidayParam(
            year = 2023,
            month = 2,
            day = 11,
            checkType = CheckType.CONSENSUS.value,
            onSuccess = {
                assertTrue(it)
            },
            onFailure = {}
        )

        val flow = flow {
            val mockResult =
                listOf(
                    BaseResponse.Success(listOf(BaseHoliday("Test Holiday", "2023-01-01", "Public"))),
                    BaseResponse.Success(listOf(BaseHoliday("Test Holiday", "2023-01-01", "Public"))),
                    BaseResponse.Success(emptyList())
                )
            emit(mockResult)
        }

        coEvery { calendarRepository.getHoliday(any(), any(), any(), any()) } returns flow

        checkHolidayUseCase.invoke(param = inputParam)
    }

    @Test
    fun testCheckHolidayFailConsensus() = runTest {
        val inputParam = CheckHolidayParam(
            year = 2023,
            month = 2,
            day = 11,
            checkType = CheckType.CONSENSUS.value,
            onSuccess = {
                assertFalse(it)
            },
            onFailure = {}
        )

        val flow = flow {
            val mockResult =
                listOf(
                    BaseResponse.Success(listOf(BaseHoliday("Test Holiday", "2023-01-01", "Public"))),
                    BaseResponse.Success(emptyList()),
                    BaseResponse.Success(emptyList())
                )
            emit(mockResult)
        }

        coEvery { calendarRepository.getHoliday(any(), any(), any(), any()) } returns flow

        checkHolidayUseCase.invoke(param = inputParam)
    }

    @Test
    fun testCheckHolidayFailConsensusOnly2Results() = runTest {
        val inputParam = CheckHolidayParam(
            year = 2023,
            month = 2,
            day = 11,
            checkType = CheckType.CONSENSUS.value,
            onSuccess = {
                assertFalse(it)
            },
            onFailure = {}
        )

        val flow = flow {
            val mockResult =
                listOf(
                    BaseResponse.Success(listOf(BaseHoliday("Test Holiday", "2023-01-01", "Public"))),
                    BaseResponse.Success(emptyList()),
                )
            emit(mockResult)
        }

        coEvery { calendarRepository.getHoliday(any(), any(), any(), any()) } returns flow

        checkHolidayUseCase.invoke(param = inputParam)
    }

    @Test
    fun testCheckHolidayBothSuccessAndError() = runTest {
        val inputParam = CheckHolidayParam(
            year = 2023,
            month = 2,
            day = 11,
            checkType = CheckType.ANY.value,
            onSuccess = {
                assertTrue(it)
            },
            onFailure = {
                assertTrue(it.isNotEmpty())
            }
        )

        val flow = flow {
            val mockResult =
                listOf(
                    BaseResponse.Success(listOf(BaseHoliday("Test Holiday", "2023-01-01", "Public"))),
                    BaseResponse.Success(emptyList()),
                    BaseResponse.Error(code = 404, message = "Error message")
                )
            emit(mockResult)
        }

        coEvery { calendarRepository.getHoliday(any(), any(), any(), any()) } returns flow

        checkHolidayUseCase.invoke(param = inputParam)
    }
}