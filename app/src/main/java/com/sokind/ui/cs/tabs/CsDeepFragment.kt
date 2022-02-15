package com.sokind.ui.cs.tabs

import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.LinearLayoutManager
import com.sokind.R
import com.sokind.data.remote.edu.BaseEdu
import com.sokind.data.remote.edu.DeepEdu
import com.sokind.data.remote.edu.Edu
import com.sokind.databinding.FragmentCsDeepBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.OnEduItemClickListener
import com.sokind.util.adapter.DeepEduAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CsDeepFragment(
    private val deepList: List<DeepEdu>,
    private val startForResult: ActivityResultLauncher<Intent>,
    private val deepVisible: Boolean
) : BaseFragment<FragmentCsDeepBinding>(R.layout.fragment_cs_deep) {
    private lateinit var deepEduAdapter: DeepEduAdapter

    override fun init() {
        setBinding()
        setRecyclerView()
    }

    private fun setBinding() {
        binding.apply {
            if (deepVisible) {
                rvHomeDeepCs.visibility = View.VISIBLE
                llDeepNo.visibility = View.GONE
            } else {
                rvHomeDeepCs.visibility = View.GONE
                llDeepNo.visibility = View.VISIBLE
            }
        }
    }

    private fun setRecyclerView() {
        deepEduAdapter = DeepEduAdapter("CS")
        deepEduAdapter.deepList = deepList
        binding.rvHomeDeepCs.layoutManager =
            LinearLayoutManager(requireContext())
        binding.rvHomeDeepCs.adapter = deepEduAdapter

        deepEduAdapter.setOnItemClickListener(object : OnEduItemClickListener {
            override fun onBaseItemClick(edu: BaseEdu, pos: Int) {
                startEdu(edu, startForResult)
            }

            override fun onDeepItemClick(edu: DeepEdu, pos: Int) {
                startEdu(edu, startForResult)
            }
        })
    }

    companion object {

    }
}