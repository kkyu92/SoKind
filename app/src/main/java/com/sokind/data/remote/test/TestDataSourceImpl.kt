package com.sokind.data.remote.test

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class TestDataSourceImpl @Inject constructor(
    private val testApi: TestApi
) : TestDataSource {
    override fun test(): Single<TestResponse> {
        return testApi
            .test()
            .subscribeOn(Schedulers.io())
    }
}