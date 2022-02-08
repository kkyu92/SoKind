package com.sokind.data.repository.edu

import com.sokind.data.local.user.UserEntity
import com.sokind.data.remote.edu.EduList
import com.sokind.data.remote.edu.EduUpdateResponse
import com.sokind.data.remote.edu.NextEdu
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody

interface EduRepository {
    fun getEdu(): Single<EduList>
    fun putEdu(
        file: MultipartBody.Part,
        eduKey: Int,
        eduType: Int
    ): Single<NextEdu>
    fun getMe(): Single<UserEntity>
}