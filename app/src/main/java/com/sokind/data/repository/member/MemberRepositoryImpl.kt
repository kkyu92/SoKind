package com.sokind.data.repository.member

import com.sokind.data.local.user.UserDataSource
import com.sokind.data.local.user.UserEntity
import com.sokind.data.local.user.UserMapper.mappingRemoteDataToLocal
import com.sokind.data.remote.base.ErrorEntity
import com.sokind.data.remote.base.errorHandlerCompletable
import com.sokind.data.remote.base.errorHandlerSingle
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
import retrofit2.HttpException
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

    override fun login(loginRequest: LoginRequest): Single<Int> {
        return memberDataSource
            .login(loginRequest)
            .flatMap { response ->
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
                                response.availableYn,
                                null,
                                null,
                                null,
                                response.accessToken
                            )
                        )
                    )
                    .andThen(Single.just(response.availableYn))
            }
    }

    override fun getMe(): Single<MemberInfo> {
        return userDataSource
            .getUser()
            .flatMap { user ->
                memberDataSource
                    .getMe(user.access!!, user.memberId)
            }
            .compose(errorHandlerSingle(tokenRepository))
    }

    override fun checkCertificate(): Completable {
        return userDataSource
            .getUser()
            .flatMapCompletable { user ->
                memberDataSource
                    .checkCertificate(user.access!!, user.memberId)
            }
            .compose(errorHandlerCompletable(tokenRepository))
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
                    user.access!!,
                    EmailRequest(user.memberId, user.memberEmail!!, newEmail)
                )
            }
            .compose(errorHandlerCompletable(tokenRepository))
    }

    override fun changePw(pw: String, newPw: String): Completable {
        return userDataSource
            .getUser()
            .flatMapCompletable { user ->
                memberDataSource.changePw(
                    user.access!!,
                    PwRequest(newPw, user.memberId, user.memberKey!!, pw)
                )
            }
            .onErrorResumeNext {
                if (it is HttpException) {
                    if (it.code() == 400) {
                        return@onErrorResumeNext Completable.error(ErrorEntity.InvalidPw)
                    }
                }
                return@onErrorResumeNext Completable.error(it)
            }
            .compose(errorHandlerCompletable(tokenRepository))

    }

    override fun changeProfile(profile: MultipartBody.Part): Completable {
        return userDataSource
            .getUser()
            .flatMapCompletable { user ->
                memberDataSource.changeProfile(user.access!!, profile, user.memberId)
            }
            .compose(errorHandlerCompletable(tokenRepository))
    }

    override fun changeExtra(event: Int, email: Int, app: Int): Completable {
        return userDataSource
            .getUser()
            .flatMapCompletable { user ->
                memberDataSource.changeExtra(user.access!!, event, email, app, user.memberId)
            }
            .compose(errorHandlerCompletable(tokenRepository))
    }

    override fun logout(): Completable {
        return userDataSource
            .deleteUser()
    }

    override fun secession(reason: String): Completable {
        return userDataSource
            .getUser()
            .flatMapCompletable { user ->
                memberDataSource.secession(user.access!!, SecessionRequest(user.memberId, reason))
                    .andThen(userDataSource.deleteUser())
            }
            .compose(errorHandlerCompletable(tokenRepository))
    }
}