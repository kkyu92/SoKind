package com.sokind.data.repository.member

import com.sokind.data.local.user.UserDataSource
import com.sokind.data.local.user.UserEntity
import com.sokind.data.local.user.UserMapper.mappingRemoteDataToLocal
import com.sokind.data.remote.member.MemberDataSource
import com.sokind.data.remote.member.MemberInfo
import com.sokind.data.remote.member.change.EmailRequest
import com.sokind.data.remote.member.change.PwRequest
import com.sokind.data.remote.member.join.EmailResponse
import com.sokind.data.remote.member.join.EnterpriseInfo
import com.sokind.data.remote.member.join.EnterpriseList
import com.sokind.data.remote.member.join.JoinInfo
import com.sokind.data.remote.member.login.LoginRequest
import com.sokind.data.remote.member.login.RefreshRequest
import com.sokind.data.repository.token.TokenRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val memberDataSource: MemberDataSource,
    private val tokenRepository: TokenRepository
) : MemberRepository {
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
                                null,
                                response.accessToken
                            )
                        )
                    )
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
                        return@flatMapSingle tokenRepository
                            .checkToken()
                            .andThen(Single.just(Unit))
                    }
            }
    }

    override fun saveUser(memberInfo: MemberInfo): Completable {
        return userDataSource
            .getAccessToken()
            .flatMapCompletable { access ->
                userDataSource
                    .saveUser(mappingRemoteDataToLocal(memberInfo, access))
            }
    }

    override fun isLogin(): Single<String> {
        return userDataSource
            .isLogin()
    }

    override fun changeEmail(request: EmailRequest): Completable {
        return userDataSource
            .getAccessToken()
            .flatMapCompletable { access ->
                memberDataSource.changeEmail(access, request)
            }
            .retryWhen { error ->
                return@retryWhen error
                    .flatMapSingle {
                        return@flatMapSingle tokenRepository
                            .checkToken()
                            .andThen(Single.just(Unit))
                    }
            }
    }

    override fun changePw(request: PwRequest): Completable {
        return userDataSource
            .getAccessToken()
            .flatMapCompletable { access ->
                memberDataSource.changePw(access, request)
            }
            .retryWhen { error ->
                return@retryWhen error
                    .flatMapSingle {
                        return@flatMapSingle tokenRepository
                            .checkToken()
                            .andThen(Single.just(Unit))
                    }
            }
    }

    override fun changeProfile(profile: MultipartBody.Part): Completable {
        return userDataSource
            .getUser()
            .flatMapCompletable { user ->
                memberDataSource.changeProfile(user.access, profile, user.memberId!!)
            }
            .retryWhen { error ->
                return@retryWhen error
                    .flatMapSingle {
                        return@flatMapSingle tokenRepository
                            .checkToken()
                            .andThen(Single.just(Unit))
                    }
            }
    }

    override fun changeExtra(event: Int, email: Int, app: Int): Completable {
        return userDataSource
            .getUser()
            .flatMapCompletable { user ->
                memberDataSource.changeExtra(user.access, event, email, app, user.memberId!!)
            }
            .retryWhen { error ->
                return@retryWhen error
                    .flatMapSingle {
                        return@flatMapSingle tokenRepository
                            .checkToken()
                            .andThen(Single.just(Unit))
                    }
            }
    }
}