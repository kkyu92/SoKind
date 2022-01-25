package com.sokind.ui.cs.tabs

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.sokind.R
import com.sokind.data.remote.edu.Edu
import com.sokind.databinding.FragmentCsBaseBinding
import com.sokind.ui.EduNavActivity
import com.sokind.ui.base.BaseFragment
import com.sokind.util.*
import com.sokind.util.adapter.BaseEduAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CsBaseFragment(
    private val baseList: List<Edu>
) : BaseFragment<FragmentCsBaseBinding>(R.layout.fragment_cs_base) {

    private lateinit var showReportFragmentListener: ShowReportFragmentListener
    private lateinit var baseEduAdapter: BaseEduAdapter

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
        setBinding()
    }

    private fun setRecyclerView() {
        baseEduAdapter = BaseEduAdapter("CS")
        baseEduAdapter.baseList = baseList
        binding.rvHomeBaseCs.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHomeBaseCs.adapter = baseEduAdapter

        baseEduAdapter.setOnItemClickListener(object : OnEduItemClickListener {
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

    private fun setBinding() {
        binding.apply {

        }
    }

    companion object {

    }
}