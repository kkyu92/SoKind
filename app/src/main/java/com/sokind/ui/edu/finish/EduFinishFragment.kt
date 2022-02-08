package com.sokind.ui.edu.finish

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.remote.edu.Edu
import com.sokind.data.remote.edu.EduMapper.mappingNextEduToEdu
import com.sokind.data.remote.edu.NextEdu
import com.sokind.databinding.FragmentEduFinishBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.dialog.BottomSheetDialog
import com.sokind.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class EduFinishFragment : BaseFragment<FragmentEduFinishBinding>(R.layout.fragment_edu_finish) {
    private val viewModel by viewModels<EduFinishViewModel>()
    private lateinit var edu: Edu
    private lateinit var nextEdu: NextEdu

    override fun init() {
        edu = arguments?.getSerializable("edu") as Edu
        nextEdu = arguments?.getSerializable("nextEdu") as NextEdu
        setBinding()
    }

    private fun setBinding() {
        binding.apply {
            if (nextEdu.nextCsResult == EMPTY) {
                tvNextEdu.visibility = View.GONE
            }

            when (edu.type) {
                1 -> { // 기본응대
                    tvType.text = "기본응대 - ${edu.position}"
                    if (edu.type != nextEdu.type) {
                        tvEndComment.text = "기본응대 교육을 모두 이수하였습니다!"
                    }
                }
                2 -> { // 상황응대
                    tvType.text = "상황응대"
                    tvSubTitle.text = edu.subTitle
                }
            }
            tvTitle.text = edu.title

            btBack
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    requireActivity().finish()
                }, { it.printStackTrace() })
            llReport
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val intent = Intent().putExtra(Constants.MOVE_TO, "report")
                    requireActivity().setResult(RESULT_OK, intent)
                    requireActivity().finish()
                }, { it.printStackTrace() })
            ivRetry
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val dialog = BottomSheetDialog.newInstance(
                        getString(R.string.alert),
                        String.format(getString(R.string.alert_edu_fin, edu.title)),
                        itemClick = { okClick ->
                            if (okClick) {
                                retryEdu()
                            }
                        }
                    )
                    dialog.show(parentFragmentManager, dialog.tag)
                }, { it.printStackTrace() })
            llList
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val intent = Intent().putExtra(Constants.MOVE_TO, "cs")
                    requireActivity().setResult(RESULT_OK, intent)
                    requireActivity().finish()
                }, { it.printStackTrace() })
            tvNextEdu
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    nextEdu()
                }, { it.printStackTrace() })
        }
    }

    private fun retryEdu() {
        val action = EduFinishFragmentDirections.actionEduFinishFragmentToEduFragment(edu)
        findNavController().navigate(action)
    }

    private fun nextEdu() {
        val mappingEdu = mappingNextEduToEdu(nextEdu)
        val action = EduFinishFragmentDirections.actionEduFinishFragmentToEduFragment(mappingEdu)
        findNavController().navigate(action)
    }

    companion object {
        const val SUCCESS = "SUCCESS"
        const val EMPTY = "EMPTY"
    }
}