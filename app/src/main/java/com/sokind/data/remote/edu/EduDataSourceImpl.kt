package com.sokind.data.remote.edu

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class EduDataSourceImpl @Inject constructor(
    private val eduApi: EduApi
) : EduDataSource {
    override fun getEdu(access: String, id: String): Single<EduList> {
        return eduApi
            .getEdu(access, id)
            .subscribeOn(Schedulers.io())
    }
}