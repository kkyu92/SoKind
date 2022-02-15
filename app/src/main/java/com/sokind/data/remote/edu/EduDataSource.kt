package com.sokind.data.remote.edu

import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Query

interface EduDataSource {
    fun getEdu(access: String, id: String): Single<EduList>
    fun startEdu(
        access: String,
        key: Int,
        type: Int,
        id: String
    ): Single<StartEdu>
    fun putEdu(
        access: String,
        eduFile: MultipartBody.Part,
        eduKey: Int,
        eduType: Int,
        id: String,
        key: Int
    ): Single<NextEdu>
}