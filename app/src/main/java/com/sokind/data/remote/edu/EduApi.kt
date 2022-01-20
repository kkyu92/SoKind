package com.sokind.data.remote.edu

import com.sokind.data.remote.member.join.EnterpriseInfo
import com.sokind.data.remote.member.join.EnterpriseList
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface EduApi {
    // 모든응대 CS
    @GET("edu/allEdu/")
    fun getAllEdu(): Single<EnterpriseList>

    // 기본응대 CS
    @GET("edu/basicEdu/")
    fun getBaseEdu(
        @Query("enterprise_key") key: String
    ): Single<EnterpriseInfo>

    // 상황응대 CS
    @GET("edu/advancedEdu/")
    fun getDeepEdu(
        @Query("enterprise_key") key: String
    ): Single<EnterpriseInfo>
}