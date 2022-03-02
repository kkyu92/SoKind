package com.sokind.data.remote.common

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface CommonApi {
    // 공지사항
    @GET("common/noticeLst")
    fun getNotice(): Single<NoticeResponse>

    // 문의하기
    @POST("common/regQuestion")
    fun postInquiry(@Body request: InquiryRequest): Completable

    // 나의 문의내역
    @GET("common/myQuestion")
    fun getInquiry(@Query("memberId") id: String): Single<InquiryResponse>

    // 나의 문의내역 삭제
    @DELETE("common/delQuestion")
    fun deleteInquiry(@Query("questionId") id: Int): Completable
}