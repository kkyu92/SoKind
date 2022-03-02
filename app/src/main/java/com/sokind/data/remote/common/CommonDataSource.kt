package com.sokind.data.remote.common

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface CommonDataSource {
    fun getNotice(): Single<NoticeResponse>
    fun postInquiry(request: InquiryRequest): Completable
    fun getInquiry(id: String): Single<InquiryResponse>
    fun deleteInquiry(id: Int): Completable
}