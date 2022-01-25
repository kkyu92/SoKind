package com.sokind.util

import com.sokind.data.remote.edu.Edu

interface OnEduItemClickListener {
    fun onEduItemClick(edu: Edu, pos: Int)
}