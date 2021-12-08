package com.sokind.data.repository.test

import com.sokind.data.remote.test.TestDataSource
import com.sokind.data.remote.test.TestRequest
import com.sokind.data.remote.test.TestResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
    private val testDataSource: TestDataSource
) : TestRepository {
    override fun test(): Single<TestResponse> {
        return testDataSource
            .test()
    }
}