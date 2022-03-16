package com.sokind.data.remote.base

object ErrorEntity {
    object FailLogin: Throwable()
    object SecessionLoginRequest: Throwable()
    object SecessionLogin: Throwable()
    object InvalidPw: Throwable()
    object NoReportData: Throwable()
}