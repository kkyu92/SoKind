package com.sokind.data.repository.member

import com.sokind.data.remote.member.MemberInfo
import com.sokind.data.remote.member.join.EmailResponse
import com.sokind.data.remote.member.join.EnterpriseInfo
import com.sokind.data.remote.member.join.EnterpriseList
import com.sokind.data.remote.member.join.JoinInfo
import com.sokind.data.remote.member.login.LoginRequest
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody

interface MemberRepository {
    fun searchEnterpriseList(keyword: String): Single<EnterpriseList>
    fun getEnterpriseInfo(code: String, key: Int): Single<EnterpriseInfo>
    fun sendEmail(email: String): Single<EmailResponse>
    fun checkId(id: String): Completable
    fun signUp(joinInfo: JoinInfo): Completable
    fun login(loginRequest: LoginRequest): Single<Int>
    fun getMe(): Single<MemberInfo>
    fun checkCertificate(): Completable
    fun saveUser(memberInfo: MemberInfo): Completable
    fun isLogin(): Single<String>
    fun changeEmail(newEmail: String): Completable
    fun changePw(pw: String, newPw: String): Completable
    fun changeProfile(profile: MultipartBody.Part): Completable
    fun changeExtra(event: Int, email: Int, app: Int): Completable
    fun logout(): Completable
    fun secession(reason: String): Completable
}