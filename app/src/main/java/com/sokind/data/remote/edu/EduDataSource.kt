package com.sokind.data.remote.edu

import io.reactivex.rxjava3.core.Single

interface EduDataSource {
    fun getEdu(access: String, id: String): Single<EduList>
}