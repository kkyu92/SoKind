package com.sokind.data.remote.report

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportApi {
    // report api
    @GET("edu/eduReport")
    fun getReport(
        @Query("enterpriseKey") key: Int,
        @Query("memberId") id: String
    ): Single<ReportResponse>
}