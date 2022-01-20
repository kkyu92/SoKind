package com.sokind.data.repository.home

import io.reactivex.rxjava3.core.Completable

interface HomeRepository {
    fun getMe(): Completable
}