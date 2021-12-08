package com.sokind.data.remote.test

import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET

interface TestApi {
    @GET("5dcfd948-f297-4ff5-9d86-573f08a3a802/")
    fun test() : Single<TestResponse>

}