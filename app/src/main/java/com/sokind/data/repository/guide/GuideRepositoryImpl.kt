package com.sokind.data.repository.guide

import com.sokind.data.local.user.UserDataSource
import com.sokind.data.remote.guide.GuideDataSource
import com.sokind.data.remote.guide.ManualResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GuideRepositoryImpl @Inject constructor(
    private val guideDataSource: GuideDataSource,
    private val userDataSource: UserDataSource
) : GuideRepository {
    override fun getManual(): Single<ManualResponse> {
        return userDataSource
            .getUser()
            .flatMap {
                guideDataSource
                    .getManual(it.memberId!!)
            }
    }
}