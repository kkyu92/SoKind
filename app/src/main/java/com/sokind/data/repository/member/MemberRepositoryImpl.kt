package com.sokind.data.repository.member

import com.sokind.data.local.user.UserDataSource
import com.sokind.data.local.user.UserEntity
import com.sokind.data.local.user.UserMapper.mappingRemoteDataToLocal
import com.sokind.data.remote.member.MemberDataSource
import com.sokind.data.remote.member.MemberInfo
import com.sokind.data.remote.member.info.EmailRequest
import com.sokind.data.remote.member.info.PwRequest
import com.sokind.data.remote.member.info.SecessionRequest
import com.sokind.data.remote.member.join.EmailResponse
import com.sokind.data.remote.member.join.EnterpriseInfo
import com.sokind.data.remote.member.join.EnterpriseList
import com.sokind.data.remote.member.join.JoinInfo
import com.sokind.data.remote.member.login.LoginRequest
import com.sokind.data.repository.token.TokenRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import timber.log.Timber
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

    override fun changeEmail(newEmail: String): Completable {
        return userDataSource
            .getUser()
            .flatMapCompletable { user ->
                memberDataSource.changeEmail(
                    user.access,
                    EmailRequest(user.memberId!!, user.memberEmail!!, newEmail)
                )
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

    override fun changePw(pw: String, newPw: String): Completable {
        return userDataSource
            .getUser()
            .flatMapCompletable { user ->
                memberDataSource.changePw(
                    user.access,
                    PwRequest(newPw, user.memberId!!, user.memberKey!!, pw)
                )
//                    .onErrorResumeNext {
//                        val status = it as HttpException
//                        if (status.code() == 400) {
//                            return@onErrorResumeNext
//                        }
//                    }
            }
            .retryWhen { error ->
                return@retryWhen error
                    .flatMapSingle {
                        Timber.e("error : ${it.localizedMessage}")
                        Timber.e("it : ${it.message}")
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

    override fun logout(): Completable {
        return userDataSource
            .deleteUser()
    }

    override fun secession(reason: String): Completable {
        return userDataSource
            .getUser()
            .flatMapCompletable { user ->
                memberDataSource.secession(user.access, SecessionRequest(user.memberId!!, reason))
                    .andThen(userDataSource.deleteUser())
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