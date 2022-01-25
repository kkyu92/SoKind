package com.sokind.ui.cs.tabs

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.sokind.R
import com.sokind.data.remote.edu.Edu
import com.sokind.databinding.FragmentCsDeepBinding
import com.sokind.ui.EduNavActivity
import com.sokind.ui.base.BaseFragment
import com.sokind.util.BottomSheetDialog
import com.sokind.util.Constants
import com.sokind.util.OnEduItemClickListener
import com.sokind.util.ShowReportFragmentListener
import com.sokind.util.adapter.DeepEduAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CsDeepFragment(
    private val deepList: List<Edu>
) : BaseFragment<FragmentCsDeepBinding>(R.layout.fragment_cs_deep) {

    private lateinit var showReportFragmentListener: ShowReportFragmentListener
    private lateinit var deepEduAdapter: DeepEduAdapter

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val go = it.data?.getStringExtra(Constants.MOVE_TO)
            Timber.e("data: ${ it.data}")
            Timber.e("go to : $go")
            when(go) {
                "cs" -> showToast("reload")
                "report" -> showReportFragmentListener.showReportFragment()
            }
        }
    }

    override fun init() {
        setRecyclerView()
    }

    private fun setRecyclerView() {
        deepEduAdapter = DeepEduAdapter("CS")
        deepEduAdapter.deepList = deepList
        binding.rvHomeDeepCs.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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