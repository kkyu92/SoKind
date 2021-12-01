package com.sokind.util

import android.Manifest

object Constants {
    const val MILLISECONDS = 1000L
    const val THROTTLE = 2000L
    const val PERMISSIONS_DATA = 0

    val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO
    )
}