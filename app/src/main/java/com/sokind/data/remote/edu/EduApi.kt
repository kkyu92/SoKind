package com.sokind.data.remote.edu

import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface EduApi {
    // 회원의 교육진행상황
    @GET("edu/eduProgress/")
    fun getEdu(
        @Header("accessToken") access: String,
        @Query("member_id") id: String
    ): Single<EduList>

    // 교육시작시 정보 불러오기
    @GET("edu/eduDetail/")
    fun startEdu(
        @Header("accessToken") access: String,
        @Query("eduKey") key: Int,
        @Query("eduType") type: Int,
        @Query("memberId") id: String
    ): Single<StartEdu>

    // 교육 업데이트
    @Multipart
    @PUT("edu/eduResult")
    fun putEdu(
        @Header("accessToken") access: String,
        @Part eduFile: MultipartBody.Part,
        @Query("eduKey") eduKey: Int,
        @Query("eduType") eduType: Int,
        @Query("memberId") id: String,
        @Query("memberKey") key: Int,
    ): Single<NextEdu>
}