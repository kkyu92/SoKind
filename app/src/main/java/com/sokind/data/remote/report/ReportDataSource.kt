package com.sokind.data.remote.report

import io.reactivex.rxjava3.core.Single

interface ReportDataSource {
    fun getReport(key: Int, id: String): Single<ReportResponse>
}