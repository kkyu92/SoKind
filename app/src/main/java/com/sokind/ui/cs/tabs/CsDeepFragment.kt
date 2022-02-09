package com.sokind.ui.cs.tabs

import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.LinearLayoutManager
import com.sokind.R
import com.sokind.data.remote.edu.Edu
import com.sokind.databinding.FragmentCsDeepBinding
import com.sokind.ui.EduNavActivity
import com.sokind.ui.base.BaseFragment
import com.sokind.util.OnEduItemClickListener
import com.sokind.util.adapter.DeepEduAdapter
import com.sokind.util.dialog.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CsDeepFragment(
    private val deepList: List<Edu>,
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
            override fun onEduItemClick(edu: Edu, pos: Int) {
                when (edu.status) {
                    1 -> { // 교육완료
                        val dialog = BottomSheetDialog.newInstance(
                            getString(R.string.alert),
                            String.format(getString(R.string.alert_edu_fin, edu.title)),
                            itemClick = { if (it) { startEdu(edu) } }
                        )
                        dialog.show(parentFragmentManager, dialog.tag)
                    }
                    2 -> { // 미교육
                        startEdu(edu)
                    }
                    3 -> { // 진행중

                    }
                    4 -> { // 분석중

                    }
                    5 -> { // 분석오류

                    }
                }
            }
        })
    }

    private fun startEdu(edu: Edu) {
        val intent = Intent(requireContext(), EduNavActivity::class.java)
        intent.putExtra("edu", edu)
        startForResult.launch(intent)
    }

    companion object {

    }
}