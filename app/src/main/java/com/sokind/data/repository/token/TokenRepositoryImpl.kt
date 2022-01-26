package com.sokind.data.repository.token

import com.sokind.data.local.user.UserDataSource
import com.sokind.data.remote.member.MemberDataSource
import com.sokind.data.remote.member.login.RefreshRequest
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor (
    private val userDataSource: UserDataSource,
    private val memberDataSource: MemberDataSource
) : TokenRepository {
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
}