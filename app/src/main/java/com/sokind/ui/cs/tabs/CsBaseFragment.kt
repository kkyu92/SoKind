package com.sokind.ui.cs.tabs

import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.LinearLayoutManager
import com.sokind.R
import com.sokind.data.remote.edu.Edu
import com.sokind.databinding.FragmentCsBaseBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.OnEduItemClickListener
import com.sokind.util.adapter.BaseEduAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CsBaseFragment(
    private val baseList: List<Edu>,
    private val startForResult: ActivityResultLauncher<Intent>
) : BaseFragment<FragmentCsBaseBinding>(R.layout.fragment_cs_base) {
    private lateinit var baseEduAdapter: BaseEduAdapter

    override fun init() {
        setRecyclerView()
        setBinding()
    }

    private fun setRecyclerView() {
        baseEduAdapter = BaseEduAdapter("CS")
        baseEduAdapter.baseList = baseList

        binding.apply {
            rvHomeBaseCs.layoutManager = LinearLayoutManager(requireContext())
            rvHomeBaseCs.adapter = baseEduAdapter

            if (baseList.isEmpty()) {
                llBaseNo.visibility = View.VISIBLE
                rvHomeBaseCs.visibility = View.GONE
            } else {
                llBaseNo.visibility = View.GONE
                rvHomeBaseCs.visibility = View.VISIBLE
            }
        }

        baseEduAdapter.setOnItemClickListener(object : OnEduItemClickListener {
            override fun onEduItemClick(edu: Edu, pos: Int) {
                startEdu(edu, startForResult)
            }
        })
    }

    private fun setBinding() {
        binding.apply {

        }
    }

    companion object {

    }
}