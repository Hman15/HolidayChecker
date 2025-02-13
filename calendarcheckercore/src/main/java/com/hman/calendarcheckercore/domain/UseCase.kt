package com.hman.calendarcheckercore.domain

abstract class UseCase<in T, out R> {
    abstract suspend fun invoke(param: T): R
}