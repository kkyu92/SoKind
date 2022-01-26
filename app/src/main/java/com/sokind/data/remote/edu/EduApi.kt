package com.sokind.data.remote.edu

import com.sokind.data.remote.member.join.EnterpriseInfo
import com.sokind.data.remote.member.join.EnterpriseList
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface EduApi {
    // 모든응대 CS
    @GET("edu/allEdu/")
    fun getAllEdu(): Single<EnterpriseList>

    // 기본응대 CS
    @GET("edu/basicEdu/")
    fun getBaseEdu(
        @Query("enterprise_key") key: String
    ): Single<EnterpriseInfo>

    // 상황응대 CS
    @GET("edu/advancedEdu/")
    fun getDeepEdu(
        @Query("enterprise_key") key: String
    ): Single<EnterpriseInfo>

    // 회원의 교육진행상황
    @GET("edu/eduProgress/")
    fun getEdu(
        @Header("accessToken") access: String,
        @Query("member_id") id: String
    ): Single<EduList>

    // 교육 업데이트
    @Multipart
    @PUT("edu/eduResult")
    fun putEdu(
        @Part eduFile: MultipartBody.Part,
        @Query("eduKey") eduKey: Int,
        @Query("eduType") eduType: Int,
        @Query("memberId") id: String,
        @Query("memberKey") key: Int,
    ): Single<EduUpdateResponse>
}