package com.sokind.data.repository.report

import com.sokind.data.local.user.UserDataSource
import com.sokind.data.local.user.UserEntity
import com.sokind.data.remote.report.ReportDataSource
import com.sokind.data.remote.report.ReportDetail
import com.sokind.data.remote.report.ReportResponse
import com.sokind.data.repository.token.TokenRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val reportDataSource: ReportDataSource,
    private val tokenRepository: TokenRepository
) : ReportRepository {
    override fun getReport(): Single<ReportResponse> {
        return userDataSource
            .getUser()
            .flatMap { user ->
                reportDataSource
                    .getReport(user.access, user.enterpriseKey!!, user.memberId!!)
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

    override fun getMe(): Single<UserEntity> {
        return userDataSource
            .getUser()
    }

    override fun getReportDetail(key: Int, type: Int): Single<ReportDetail> {
        return userDataSource
            .getUser()
            .flatMap { user ->
                reportDataSource
                    .detailReport(user.access, key, type, user.memberId!!)
            }
//            .retryWhen { error ->
//                return@retryWhen error
//                    .flatMapSingle {
//                        return@flatMapSingle tokenRepository
//                            .checkToken()
//                            .andThen(Single.just(Unit))
//                    }
//            }
    }
}