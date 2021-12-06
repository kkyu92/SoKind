package com.sokind.util

import android.Manifest
import android.util.Patterns
import java.util.regex.Pattern

object Constants {
    const val MILLISECONDS = 1000L
    const val THROTTLE = 2000L
    const val BACK_BTN_EXIT_TIMEOUT = 2500L
    const val PERMISSIONS_DATA = 0

    val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO
    )

    fun validateId(id: String): Boolean {
        val idRegex = Pattern.compile("^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{7,20}\$")
        return idRegex.matcher(id).find()
    }
    fun validatePw(pw: String): Boolean {
        val pwRegex = Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@\$!%*#?&]).{7,20}.\$")
        return pwRegex.matcher(pw).find()
    }
    fun validateEmail(email: String): Boolean {
        val emailRegex = Patterns.EMAIL_ADDRESS
        return emailRegex.matcher(email).find()
    }
}