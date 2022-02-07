package com.sokind.data.remote.guide

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GuideDataSourceImpl @Inject constructor(
    private val guideApi: GuideApi
) : GuideDataSource {
    override fun getManual(id: String): Single<ManualResponse> {
        return guideApi
            .getManual(id)
            .subscribeOn(Schedulers.io())
    }
}