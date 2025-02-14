package com.hman.calendarcheckercore.presentation

import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    HolidayChecker.initialize(
        "sQrYuRQSX1Bi1xrmWFQ1W1vEQ0m4DtW2",
        "60f06c460e9245589ed865627e740af5",
        "6ea999b5-3360-45ae-944d-4b4ab9307946"
    )


    println("Enter year: ")
    val year = readlnOrNull()?.toIntOrNull() ?: 2023
    println("Enter month: ")
    val month = readlnOrNull()?.toIntOrNull() ?: 1
    println("Enter day: ")
    val day = readlnOrNull()?.toIntOrNull() ?: 1
    println("Enter check state: ")
    val state = readlnOrNull()?.toIntOrNull() ?: 1

    HolidayChecker.isHoliday(
        year = year,
        month = month,
        day = day,
        checkType = state,
        onSuccess = {
            println("Is Holiday: $it")
        },
        onFailure = {
            println("$it")
        })

}