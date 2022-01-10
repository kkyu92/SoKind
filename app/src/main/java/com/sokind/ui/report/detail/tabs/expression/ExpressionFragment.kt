package com.sokind.ui.report.detail.tabs.expression

import androidx.fragment.app.viewModels
import com.sokind.R
import com.sokind.databinding.FragmentExpressionBinding
import com.sokind.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpressionFragment : BaseFragment<FragmentExpressionBinding>(R.layout.fragment_expression) {
    private val viewModel by viewModels<ExpressionViewModel>()

    override fun init() {

    }

    companion object {
        fun newInstance() = ExpressionFragment()
    }

}