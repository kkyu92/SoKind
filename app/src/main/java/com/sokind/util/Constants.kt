package com.sokind.util

import android.Manifest
import android.content.Context
import android.util.Patterns
import android.util.TypedValue
import java.util.regex.Pattern

object Constants {
    const val FILE_NAME_FORMAT = "yy-MM-dd-HH-mm-ss-SSS"
    const val MOVE_TO = "move_to"
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
        val idRegex = Pattern.compile("^(?=.*[a-zA-Z])[A-Za-z0-9_\$-]{6,12}\$")
        return idRegex.matcher(id).find()
    }

    fun validatePw(pw: String): Boolean {
        val pwRegex = Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\$@!%*#?&]).{7,20}.\$")
        return pwRegex.matcher(pw).find()
    }

    fun validateEmail(email: String): Boolean {
        val emailRegex = Patterns.EMAIL_ADDRESS
        return emailRegex.matcher(email).find()
    }

    fun validateKo(text: String): Boolean {
        val koRegex = Pattern.compile("^[가-힣\\s]*$")
        return koRegex.matcher(text).find()
    }

    fun validateEn(text: String): Boolean {
        val enRegex = Pattern.compile("^[a-zA-Z\\s]*$")
        return enRegex.matcher(text).find()
    }

    fun getAddLinearBack(context: Context, percent: Int, dp: Float): Int {
        var addHeight = 0
        when (percent) {
            in 1..20 -> {
                addHeight = dpToPixel(context, dp)
            }
            in 21..40 -> {
                addHeight = dpToPixel(context, dp) * 2
            }
            in 41..60 -> {
                addHeight = dpToPixel(context, dp) * 3
            }
            in 61..80 -> {
                addHeight = dpToPixel(context, dp) * 4
            }
            in 81..100 -> {
                addHeight = dpToPixel(context, dp) * 5
            }
        }
        return addHeight
    }

    private fun dpToPixel(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}