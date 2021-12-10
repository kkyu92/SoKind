package com.sokind.data.repository.test

import com.sokind.data.remote.test.TestResponse
import io.reactivex.rxjava3.core.Single

interface TestRepository {
    fun test(): Single<TestResponse>
}