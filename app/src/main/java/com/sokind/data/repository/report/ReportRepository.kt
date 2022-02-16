package com.sokind.data.repository.report

import com.sokind.data.local.user.UserEntity
import com.sokind.data.remote.report.ReportDetail
import com.sokind.data.remote.report.ReportResponse
import io.reactivex.rxjava3.core.Single

interface ReportRepository {
    fun getReport(): Single<ReportResponse>
    fun getMe(): Single<UserEntity>
    fun getReportDetail(key: Int, type: Int): Single<ReportDetail>
}