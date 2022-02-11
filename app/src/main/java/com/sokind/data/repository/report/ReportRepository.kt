package com.sokind.data.repository.report

import com.sokind.data.remote.report.ReportResponse
import io.reactivex.rxjava3.core.Single

interface ReportRepository {
    fun getReport(): Single<ReportResponse>
}