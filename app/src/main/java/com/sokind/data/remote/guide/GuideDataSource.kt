package com.sokind.data.remote.guide

import io.reactivex.rxjava3.core.Single

interface GuideDataSource {
    fun getManual(id: String): Single<ManualResponse>
}