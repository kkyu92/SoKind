package com.sokind.data.repository.guide

import com.sokind.data.remote.guide.ManualResponse
import io.reactivex.rxjava3.core.Single

interface GuideRepository {
    fun getManual(): Single<ManualResponse>
}