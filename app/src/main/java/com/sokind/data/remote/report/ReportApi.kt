package com.sokind.data.remote.report

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ReportApi {
    // report tap
    @GET("edu/eduReport")
    fun getReport(
        @Header("accessToken") access: String,
        @Query("enterpriseKey") key: Int,
        @Query("memberId") id: String
    ): Single<ReportResponse>

    // report 디테일
    @GET("edu/eduReportDetail")
    fun detailReport(
        @Header("accessToken") access: String,
        @Query("eduKey") key: Int,
        @Query("eduType") type: Int,
        @Query("memberId") id: String
    ): Single<ReportDetail>
}