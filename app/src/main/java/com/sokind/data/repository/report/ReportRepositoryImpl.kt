package com.sokind.data.repository.report

import com.sokind.data.local.user.UserDataSource
import com.sokind.data.local.user.UserEntity
import com.sokind.data.remote.base.ErrorEntity
import com.sokind.data.remote.base.errorHandlerSingle
import com.sokind.data.remote.report.ReportDataSource
import com.sokind.data.remote.report.ReportDetail
import com.sokind.data.remote.report.ReportResponse
import com.sokind.data.repository.token.TokenRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.HttpException
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
            .onErrorResumeNext {
                if (it is HttpException) {
                    if (it.code() == 400) {
                        return@onErrorResumeNext Single.error(ErrorEntity.NoReportData)
                    }
                }
                return@onErrorResumeNext Single.error(it)
            }
            .compose(errorHandlerSingle(tokenRepository))
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
            .compose(errorHandlerSingle(tokenRepository))
    }
}