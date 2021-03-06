package com.sokind.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import com.sokind.R
import com.sokind.data.remote.edu.Edu
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EduNavActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edu_nav)
        val eduData = intent.getSerializableExtra("edu") as Edu
        val bundle = bundleOf("edu" to eduData)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView_edu) as NavHostFragment
        val navController = navHostFragment.navController
        navController.setGraph(R.navigation.edu_nav, bundle)
    }
}