package com.sokind.data.repository.edu

import com.sokind.data.local.user.UserDataSource
import com.sokind.data.local.user.UserMapper
import com.sokind.data.remote.edu.EduDataSource
import com.sokind.data.remote.edu.EduList
import com.sokind.data.remote.member.MemberDataSource
import com.sokind.data.remote.member.login.RefreshRequest
import com.sokind.data.repository.member.MemberRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
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
}