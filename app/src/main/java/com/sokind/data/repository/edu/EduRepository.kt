package com.sokind.data.repository.edu

import com.sokind.data.remote.edu.EduList
import io.reactivex.rxjava3.core.Single

interface EduRepository {
    fun getEdu(): Single<EduList>
}