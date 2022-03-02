package com.sokind.data.repository.common

import com.sokind.data.remote.common.InquiryRequest
import com.sokind.data.remote.common.InquiryResponse
import com.sokind.data.remote.common.NoticeResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface CommonRepository {
    fun getNotice(): Single<NoticeResponse>
    fun postInquiry(type: String, title: String, contents:String): Completable
    fun getInquiry(): Single<InquiryResponse>
    fun deleteInquiry(id: Int): Completable
}