package com.sokind.data.remote.report

import io.reactivex.rxjava3.core.Single

interface ReportDataSource {
    fun getReport(access: String, key: Int, id: String): Single<ReportResponse>

    fun detailReport(
        access: String,
        key: Int,
        type: Int,
        id: String,
    ): Single<ReportDetail>
}