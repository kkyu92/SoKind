package com.sokind.data.remote.report

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ReportDataSourceImpl @Inject constructor(
    private val reportApi: ReportApi
) : ReportDataSource {
    override fun getReport(access: String,key: Int, id: String): Single<ReportResponse> {
        return reportApi
            .getReport(access, key, id)
            .subscribeOn(Schedulers.io())
    }

    override fun detailReport(access: String, key: Int, type: Int, id: String): Single<ReportDetail> {
        return reportApi
            .detailReport(access, key, type, id)
            .subscribeOn(Schedulers.io())
    }
}