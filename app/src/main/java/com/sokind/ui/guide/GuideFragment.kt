package com.sokind.ui.guide

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sokind.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GuideFragment : Fragment() {

    companion object {
        fun newInstance() = GuideFragment()
    }

    private lateinit var viewModel: GuideViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_guide, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GuideViewModel::class.java)
        // TODO: Use the ViewModel
    }

}