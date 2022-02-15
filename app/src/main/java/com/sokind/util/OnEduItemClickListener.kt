package com.sokind.util

import com.sokind.data.remote.edu.BaseEdu
import com.sokind.data.remote.edu.DeepEdu

interface OnEduItemClickListener {
    fun onBaseItemClick(edu: BaseEdu, pos: Int)
    fun onDeepItemClick(edu: DeepEdu, pos: Int)
}