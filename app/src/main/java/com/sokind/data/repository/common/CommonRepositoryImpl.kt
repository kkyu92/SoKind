package com.sokind.data.repository.common

import com.sokind.data.local.user.UserDataSource
import com.sokind.data.remote.common.CommonDataSource
import com.sokind.data.remote.common.InquiryRequest
import com.sokind.data.remote.common.InquiryResponse
import com.sokind.data.remote.common.NoticeResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class CommonRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val commonDataSource: CommonDataSource
) : CommonRepository {
    override fun getNotice(): Single<NoticeResponse> {
        return commonDataSource
            .getNotice()
    }

    override fun postInquiry(type: String, title: String, contents: String): Completable {
        return userDataSource
            .getUser()
            .flatMapCompletable { user ->
                commonDataSource
                    .postInquiry(InquiryRequest(user.memberId!!, type, title, contents))
            }
    }

    override fun getInquiry(): Single<InquiryResponse> {
        return userDataSource
            .getUser()
            .flatMap { user ->
                commonDataSource
                    .getInquiry(user.memberId!!)
            }
    }

    override fun deleteInquiry(id: Int): Completable {
        return commonDataSource
            .deleteInquiry(id)
    }
}