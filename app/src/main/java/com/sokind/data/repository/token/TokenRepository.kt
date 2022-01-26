package com.sokind.data.repository.token

import io.reactivex.rxjava3.core.Completable

// token 으로 만들어서 호출
interface TokenRepository {
    fun checkToken(): Completable
}