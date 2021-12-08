package com.sokind.data.remote.test

import io.reactivex.rxjava3.core.Single

interface TestDataSource {
    fun test() : Single<TestResponse>
}