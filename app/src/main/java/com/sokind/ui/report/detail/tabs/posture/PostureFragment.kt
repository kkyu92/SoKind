package com.sokind.ui.report.detail.tabs.posture

import androidx.fragment.app.viewModels
import com.sokind.R
import com.sokind.data.remote.report.ReportPosture
import com.sokind.databinding.FragmentPostureBinding
import com.sokind.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostureFragment(
    private val postureData: ReportPosture
) : BaseFragment<FragmentPostureBinding>(R.layout.fragment_posture) {
    private val viewModel by viewModels<PostureViewModel>()

    override fun init() {
        binding.apply {
            count1.text = getString(R.string.pose_cnt, postureData.headRotate.toString())
            count2.text = getString(R.string.pose_cnt, postureData.headShake.toString())
            count3.text = getString(R.string.pose_cnt, postureData.headTurn.toString())
            count4.text = getString(R.string.pose_cnt, postureData.shoulderShake.toString())
            count5.text = getString(R.string.pose_cnt, postureData.shoulderTurn.toString())

            count1Pose.text = getString(R.string.pose_cnt, postureData.headShake.toString())
            count2Pose.text = getString(R.string.pose_cnt, postureData.headTurn.toString())
            count3Pose.text = getString(R.string.pose_cnt, postureData.headRotate.toString())

            count4Shoulder.text = getString(R.string.pose_cnt, postureData.shoulderShake.toString())
            count5Shoulder.text = getString(R.string.pose_cnt, postureData.shoulderTurn.toString())
        }
    }
}