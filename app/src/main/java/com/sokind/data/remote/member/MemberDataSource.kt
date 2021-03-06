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

interface MemberDataSource {
    fun searchEnterpriseList(keyword: String): Single<EnterpriseList>
    fun getEnterpriseInfo(code: String, key: Int): Single<EnterpriseInfo>
    fun sendEmail(email: String): Single<EmailResponse>
    fun checkId(id: String): Completable
    fun signUp(joinInfo: JoinInfo): Completable
    fun login(loginRequest: LoginRequest): Single<LoginResponse>
    fun refreshToken(access: String, request: RefreshRequest): Single<RefreshResponse>
    fun checkToken(access: String, id: String): Completable
    fun getMe(access: String, id: String): Single<MemberInfo>
    fun checkCertificate(access: String, id: String): Completable
    fun changeEmail(access: String, request: EmailRequest): Completable
    fun changePw(access: String, request: PwRequest): Completable
    fun changeProfile(access: String, profile: MultipartBody.Part, id: String): Completable
    fun changeExtra(access: String, event: Int, email: Int, app: Int, id: String): Completable
    fun secession(access: String, request: SecessionRequest): Completable
}