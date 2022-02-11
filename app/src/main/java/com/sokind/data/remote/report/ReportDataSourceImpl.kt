package com.sokind.data.remote.report

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ReportDataSourceImpl @Inject constructor(
    private val reportApi: ReportApi
) : ReportDataSource {
    override fun getReport(key: Int, id: String): Single<ReportResponse> {
        return reportApi
            .getReport(key, id)
            .subscribeOn(Schedulers.io())
    }
}