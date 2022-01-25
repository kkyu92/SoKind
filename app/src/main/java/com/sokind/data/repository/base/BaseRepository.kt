package com.sokind.data.repository.base

import io.reactivex.rxjava3.core.Completable

interface BaseRepository {
    fun checkToken(): Completable
}