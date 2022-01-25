package com.sokind.data.repository.edu

import com.sokind.data.local.user.UserDataSource
import com.sokind.data.remote.edu.EduDataSource
import com.sokind.data.remote.edu.EduList
import com.sokind.data.repository.member.MemberRepository
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class EduRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val memberRepository: MemberRepository,
    private val eduDataSource: EduDataSource
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
                        return@flatMapSingle memberRepository
                            .checkToken()
                            .andThen(Single.just(Unit))
                    }
            }
    }

    override fun putEdu(
        file: MultipartBody.Part,
        eduKey: Int,
        eduType: Int,
        id: Int,
        key: Int
    ): Single<String> {
        return eduDataSource
            .putEdu(file, eduKey, eduType, id, key)
    }
}