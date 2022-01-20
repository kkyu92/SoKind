package com.sokind.data.remote.member

import com.sokind.data.remote.member.join.EmailResponse
import com.sokind.data.remote.member.join.EnterpriseInfo
import com.sokind.data.remote.member.join.EnterpriseList
import com.sokind.data.remote.member.join.JoinInfo
import com.sokind.data.remote.member.login.LoginRequest
import com.sokind.data.remote.member.login.LoginResponse
import com.sokind.data.remote.member.login.RefreshRequest
import com.sokind.data.remote.member.login.RefreshResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface MemberApi {
    // 기업 리스트
    @GET("enterprise/searchEnterprise/")
    fun searchEnterpriseList(@Query("keyword") keyword: String): Single<EnterpriseList>

    // 기업 상세정보 (점포 + 직위)
    @GET("enterprise/enterpriseinfo/")
    fun getEnterpriseInfo(
        @Query("enterprise_code") code: String,
        @Query("enterprise_key") key: Int
    ): Single<EnterpriseInfo>

    // 이메일 인증
    @POST("member/sendApprovalMail/")
    fun sendEmail(@Query("emailAddr") email: String): Single<EmailResponse>

    // 아이디 중복확인
    @GET("member/dupeChk/")
    fun checkId(@Query("checkid") id: String): Completable

    // 회원가입
    @POST("member/signup/")
    fun signUp(@Body joinInfo: JoinInfo) : Completable

//    // 기업 하위 점포정보
//    @GET("enterprise/storeinfo/")
//    fun getStoreInfo(
//        @Query("enterprise_key") enterpriseKey: String,
//        @Query("store_key") storeKey: String
//    ): Single<StoreList>
//
//    // 기업 직위정보
//    @GET("enterprise/positioninfo/")
//    fun getPositionInfo(@Query("enterprise_key") enterpriseKey: String): Single<PositionList>

    // 로그인
    @POST("member/memberLogin/")
    fun login(@Body loginRequest: LoginRequest): Single<LoginResponse>

    // 토큰갱신
    @POST("member/reIssueToken/")
    fun refreshToken(
        @Header("accessToken") access: String,
        @Body request: RefreshRequest
    ): Single<RefreshResponse>

    // 토큰유효성 검사
    @POST("member/tokenChk/")
    fun checkToken(
        @Header("accessToken") access: String,
        @Query("memberId") id: String
    ): Completable

    // 유저 정보
    @GET("member/getMemberinfo/")
    fun getMe(
        @Header("accessToken") access: String,
        @Query("memberid") id: String
    ): Single<MemberInfo>
}