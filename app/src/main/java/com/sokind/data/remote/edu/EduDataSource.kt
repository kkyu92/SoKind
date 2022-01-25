package com.sokind.data.remote.edu

import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody

interface EduDataSource {
    fun getEdu(access: String, id: String): Single<EduList>
    fun putEdu(
        file: MultipartBody.Part,
        eduKey: Int,
        eduType: Int,
        id: Int,
        key: Int
    ): Single<String>
}