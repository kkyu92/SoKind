package com.sokind.data.remote.guide

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GuideApi {
    // 우리회사 메뉴얼
    @GET("common/enterpriseGuide")
    fun getManual(
        @Query("memberId") id: String
    ): Single<ManualResponse>

    // 상황응대 가이드
}