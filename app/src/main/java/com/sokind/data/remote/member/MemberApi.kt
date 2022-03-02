package com.sokind.data.remote.member

import com.sokind.data.remote.member.info.EmailRequest
import com.sokind.data.remote.member.info.PwRequest
import com.sokind.data.remote.member.info.SecessionRequest
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
import okhttp3.MultipartBody
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

    // 이메일 변경
    @PUT("member/changeEmail/")
    fun changeEmail(
        @Header("accessToken") access: String,
        @Body request: EmailRequest
    ): Completable

    // 비밀번호 변경
    @PUT("member/changePw/")
    fun changePw(
        @Header("accessToken") access: String,
        @Body request: PwRequest
    ): Completable

    // 프로필 변경
    @Multipart
    @PUT("member/changeMemberProfile")
    fun changeProfile(
        @Header("accessToken") access: String,
        @Part profile: MultipartBody.Part,
        @Query("memberId") id: String,
    ): Completable

    // 부가정보 변경
    @PUT("member/changeExtraInfo")
    fun changeExtra(
        @Header("accessToken") access: String,
        @Query("eventYN") event: Int,
        @Query("emailYN") email: Int,
        @Query("pushYN") app: Int,
        @Query("memberId") id: String,
    ): Completable

    // 회원탈퇴
    @POST("member/quit")
    fun secession(
        @Header("accessToken") access: String,
        @Body request: SecessionRequest
    ): Completable
}