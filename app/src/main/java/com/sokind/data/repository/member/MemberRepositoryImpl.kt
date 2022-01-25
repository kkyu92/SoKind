package com.sokind.data.repository.member

import com.sokind.data.local.user.UserDataSource
import com.sokind.data.local.user.UserEntity
import com.sokind.data.remote.member.MemberDataSource
import com.sokind.data.remote.member.MemberInfo
import com.sokind.data.remote.member.join.EmailResponse
import com.sokind.data.remote.member.join.EnterpriseInfo
import com.sokind.data.remote.member.join.EnterpriseList
import com.sokind.data.remote.member.join.JoinInfo
import com.sokind.data.remote.member.login.LoginRequest
import com.sokind.data.remote.member.login.RefreshRequest
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val memberDataSource: MemberDataSource
): MemberRepository{
    override fun searchEnterpriseList(keyword: String): Single<EnterpriseList> {
        return memberDataSource
            .searchEnterpriseList(keyword)
    }

    override fun getEnterpriseInfo(code: String, key: Int): Single<EnterpriseInfo> {
        return memberDataSource
            .getEnterpriseInfo(code, key)
    }

    override fun sendEmail(email: String): Single<EmailResponse> {
        return memberDataSource
            .sendEmail(email)
    }

    override fun checkId(id: String): Completable {
        return memberDataSource
            .checkId(id)
    }

    override fun signUp(joinInfo: JoinInfo): Completable {
        return memberDataSource
            .signUp(joinInfo)
    }

    override fun login(loginRequest: LoginRequest): Completable {
        return memberDataSource
            .login(loginRequest)
            .flatMapCompletable { response ->
                userDataSource
                    .deleteUser()
                    .andThen(
                        userDataSource.saveUser(
                            UserEntity(
                                null,
                                loginRequest.memberId,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                response.accessToken
                            )
                        )
                    )
            }
    }

    override fun checkToken(): Completable {
        return userDataSource
            .getUser()
            .flatMapCompletable { user ->
                memberDataSource
                    .checkToken(user.access, user.memberId!!)
            }
            .retryWhen { error ->
                return@retryWhen error
                    .flatMapSingle {
                        return@flatMapSingle userDataSource
                            .getUser()
                            .flatMap { user ->
                                memberDataSource
                                    .refreshToken(user.access, RefreshRequest(user.memberId!!))
                            }
                            .flatMap { response ->
                                userDataSource.updateAccessToken(response.accessToken)
                                    .andThen(Single.just(Unit))
                            }
                    }
            }
    }

    override fun getMe(): Single<MemberInfo> {
        return userDataSource
            .getUser()
            .flatMap { user ->
                memberDataSource
                    .getMe(user.access, user.memberId!!)
            }
            .retryWhen { error ->
                return@retryWhen error
                    .flatMapSingle {
                        return@flatMapSingle checkToken()
                            .andThen(Single.just(Unit))
                    }
            }
    }

    override fun isLogin(): Single<String> {
        return userDataSource
            .isLogin()
    }
}