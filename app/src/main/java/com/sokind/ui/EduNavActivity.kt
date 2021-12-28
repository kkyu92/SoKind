package com.sokind.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sokind.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EduNavActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edu_nav)
    }
}