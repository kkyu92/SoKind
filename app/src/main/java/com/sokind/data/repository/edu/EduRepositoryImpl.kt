package com.sokind.data.repository.edu

import com.sokind.data.local.user.UserDataSource
import com.sokind.data.local.user.UserEntity
import com.sokind.data.remote.base.errorHandlerSingle
import com.sokind.data.remote.edu.*
import com.sokind.data.repository.token.TokenRepository
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class EduRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val eduDataSource: EduDataSource,
    private val tokenRepository: TokenRepository
) : EduRepository {
    override fun getEdu(): Single<EduList> {
        return userDataSource
            .getUser()
            .flatMap { user ->
                eduDataSource
                    .getEdu(user.access, user.memberId!!)
            }
            .compose(errorHandlerSingle(tokenRepository))
    }

    override fun startEdu(eduKey: Int, eduType: Int): Single<StartEdu> {
        return userDataSource
            .getUser()
            .flatMap { user ->
                eduDataSource
                    .startEdu(user.access, eduKey, eduType, user.memberId!!)
            }
            .compose(errorHandlerSingle(tokenRepository))
    }

    override fun putEdu(
        file: MultipartBody.Part,
        eduKey: Int,
        eduType: Int
    ): Single<NextEdu> {
        return userDataSource
            .getUser()
            .flatMap { user ->
                eduDataSource
                    .putEdu(user.access, file, eduKey, eduType, user.memberId!!, user.memberKey!!)
            }
            .compose(errorHandlerSingle(tokenRepository))
    }

    override fun getMe(): Single<UserEntity> {
        return userDataSource
            .getUser()
    }
}