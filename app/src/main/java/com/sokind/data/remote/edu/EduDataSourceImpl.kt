package com.sokind.data.remote.edu

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MultipartBody
import javax.inject.Inject

class EduDataSourceImpl @Inject constructor(
    private val eduApi: EduApi
) : EduDataSource {
    override fun getEdu(access: String, id: String): Single<EduList> {
        return eduApi
            .getEdu(access, id)
            .subscribeOn(Schedulers.io())
    }

    override fun putEdu(
        access: String,
        eduFile: MultipartBody.Part,
        eduKey: Int,
        eduType: Int,
        id: String,
        key: Int
    ): Single<NextEdu> {
        return eduApi
            .putEdu(access, eduFile, eduKey, eduType, id, key)
            .subscribeOn(Schedulers.io())
    }
}