package com.sokind.data.repository.edu

import com.sokind.data.local.user.UserDataSource
import com.sokind.data.remote.edu.EduDataSource
import com.sokind.data.remote.edu.EduList
import com.sokind.data.remote.edu.EduUpdateResponse
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
            .retryWhen { error ->
                return@retryWhen error
                    .flatMapSingle {
                        return@flatMapSingle tokenRepository
                            .checkToken()
                            .andThen(Single.just(Unit))
                    }
            }
    }

    override fun putEdu(
        file: MultipartBody.Part,
        eduKey: Int,
        eduType: Int
    ): Single<EduUpdateResponse> {

        return userDataSource
            .getUser()
            .flatMap { user ->
                eduDataSource
                    .putEdu(file, eduKey, eduType, user.memberId!!, user.memberKey!!)
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