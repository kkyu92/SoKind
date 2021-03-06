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
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MultipartBody
import javax.inject.Inject

class MemberDataSourceImpl @Inject constructor(
    private val memberApi: MemberApi
) : MemberDataSource {
    override fun searchEnterpriseList(keyword: String): Single<EnterpriseList> {
        return memberApi
            .searchEnterpriseList(keyword)
            .subscribeOn(Schedulers.io())
    }

    override fun getEnterpriseInfo(code: String, key: Int): Single<EnterpriseInfo> {
        return memberApi
            .getEnterpriseInfo(code, key)
            .subscribeOn(Schedulers.io())
    }

    override fun sendEmail(email: String): Single<EmailResponse> {
        return memberApi
            .sendEmail(email)
            .subscribeOn(Schedulers.io())
    }

    override fun checkId(id: String): Completable {
        return memberApi
            .checkId(id)
            .subscribeOn(Schedulers.io())
    }

    override fun signUp(joinInfo: JoinInfo): Completable {
        return memberApi
            .signUp(joinInfo)
            .subscribeOn(Schedulers.io())
    }

    override fun login(loginRequest: LoginRequest): Single<LoginResponse> {
        return memberApi
            .login(loginRequest)
            .subscribeOn(Schedulers.io())
    }

    override fun refreshToken(access: String, request: RefreshRequest): Single<RefreshResponse> {
        return memberApi
            .refreshToken(access, request)
            .subscribeOn(Schedulers.io())
    }

    override fun checkToken(access: String, id: String): Completable {
        return memberApi
            .checkToken(access, id)
            .subscribeOn(Schedulers.io())
    }

    override fun getMe(access: String, id: String): Single<MemberInfo> {
        return memberApi
            .getMe(access, id)
            .subscribeOn(Schedulers.io())
    }

    override fun checkCertificate(access: String, id: String): Completable {
        return memberApi
            .checkCertificate(access, id)
            .subscribeOn(Schedulers.io())
    }

    override fun changeEmail(access: String, request: EmailRequest): Completable {
        return memberApi
            .changeEmail(access, request)
            .subscribeOn(Schedulers.io())
    }

    override fun changePw(access: String, request: PwRequest): Completable {
        return memberApi
            .changePw(access, request)
            .subscribeOn(Schedulers.io())
    }

    override fun changeProfile(
        access: String,
        profile: MultipartBody.Part,
        id: String
    ): Completable {
        return memberApi
            .changeProfile(access, profile, id)
            .subscribeOn(Schedulers.io())
    }

    override fun changeExtra(
        access: String,
        event: Int,
        email: Int,
        app: Int,
        id: String
    ): Completable {
        return memberApi
            .changeExtra(access, event, email, app, id)
            .subscribeOn(Schedulers.io())
    }

    override fun secession(access: String, request: SecessionRequest): Completable {
        return memberApi
            .secession(access, request)
            .subscribeOn(Schedulers.io())
    }
}