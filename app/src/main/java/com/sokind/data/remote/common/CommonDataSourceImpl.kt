package com.sokind.data.remote.common

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class CommonDataSourceImpl @Inject constructor(
    private val commonApi: CommonApi
) : CommonDataSource {
    override fun getNotice(): Single<NoticeResponse> {
        return commonApi
            .getNotice()
            .subscribeOn(Schedulers.io())
    }

    override fun postInquiry(request: InquiryRequest): Completable {
        return commonApi
            .postInquiry(request)
            .subscribeOn(Schedulers.io())
    }

    override fun getInquiry(id: String): Single<InquiryResponse> {
        return commonApi
            .getInquiry(id)
            .subscribeOn(Schedulers.io())
    }

    override fun deleteInquiry(id: Int): Completable {
        return commonApi
            .deleteInquiry(id)
            .subscribeOn(Schedulers.io())
    }

}