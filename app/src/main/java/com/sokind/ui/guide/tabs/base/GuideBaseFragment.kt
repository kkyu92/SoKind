package com.sokind.ui.guide.tabs.base

import android.graphics.drawable.Drawable
import android.widget.Button
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.databinding.FragmentGuideBaseBinding
import com.sokind.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GuideBaseFragment : BaseFragment<FragmentGuideBaseBinding>(R.layout.fragment_guide_base) {
    private val viewModel by viewModels<GuideBaseViewModel>()

    private lateinit var speechEnable: Drawable
    private lateinit var speechDisable: Drawable
    private lateinit var  emotionEnable: Drawable
    private lateinit var  emotionDisable: Drawable
    private lateinit var  postureEnable: Drawable
    private lateinit var  postureDisable: Drawable

    override fun init() {
        setBinding()
    }

    private fun setBinding() {
        speechEnable = requireContext().resources.getDrawable(R.drawable.icon_speech_enable, null)
        speechDisable = requireContext().resources.getDrawable(R.drawable.icon_speech_disable, null)
        emotionEnable = requireContext().resources.getDrawable(R.drawable.icon_emotion_enable, null)
        emotionDisable = requireContext().resources.getDrawable(R.drawable.icon_emotion_disable, null)
        postureEnable = requireContext().resources.getDrawable(R.drawable.icon_pose_enable, null)
        postureDisable = requireContext().resources.getDrawable(R.drawable.icon_pose_disable, null)

        binding.apply {
            btSpeech
                .clicks()
                .subscribe({
                    isChecked(true, btSpeech, 1)
                    isChecked(false, btExpression, 2)
                    isChecked(false, btPosture, 3)
                }, { it.printStackTrace() })
            btExpression
                .clicks()
                .subscribe({
                    isChecked(false, btSpeech, 1)
                    isChecked(true, btExpression, 2)
                    isChecked(false, btPosture, 3)
                }, { it.printStackTrace() })
            btPosture
                .clicks()
                .subscribe({
                    isChecked(false, btSpeech, 1)
                    isChecked(false, btExpression, 2)
                    isChecked(true, btPosture, 3)
                }, { it.printStackTrace() })
        }
    }

    private fun isChecked(check: Boolean, button: Button, position: Int) {
        if (check) {
            button.setBackgroundResource(R.drawable.custom_btn_enabled)
            button.setTextColor(getColor(R.color.white))
            when (position) {
                1 -> {
                    button.setCompoundDrawablesWithIntrinsicBounds(speechEnable, null,null,null)
                    binding.tvTitle.text = getString(R.string.guide_base_speech_title)
                    binding.tvSubTitle.text = getString(R.string.guide_base_speech_sub_title)
                    binding.tvContent.text = getString(R.string.guide_base_speech_content)
                    binding.ivImg.setImageResource(R.drawable.img_speak)
                }
                2 -> {
                    button.setCompoundDrawablesWithIntrinsicBounds(emotionEnable, null,null,null)
                    binding.tvTitle.text = getString(R.string.guide_base_expression_title)
                    binding.tvSubTitle.text = getString(R.string.guide_base_expression_sub_title)
                    binding.tvContent.text = getString(R.string.guide_base_expression_content)
                    binding.ivImg.setImageResource(R.drawable.img_emotion)
                }
                3 -> {
                    button.setCompoundDrawablesWithIntrinsicBounds(postureEnable, null,null,null)
                    binding.tvTitle.text = getString(R.string.guide_base_posture_title)
                    binding.tvSubTitle.text = getString(R.string.guide_base_posture_sub_title)
                    binding.tvContent.text = getString(R.string.guide_base_posture_content)
                    binding.ivImg.setImageResource(R.drawable.img_posture)
                }
                else -> {
                    button.setCompoundDrawablesWithIntrinsicBounds(speechEnable, null,null,null)
                    binding.tvTitle.text = getString(R.string.guide_base_speech_title)
                    binding.tvSubTitle.text = getString(R.string.guide_base_speech_sub_title)
                    binding.tvContent.text = getString(R.string.guide_base_speech_content)
                    binding.ivImg.setImageResource(R.drawable.img_speak)
                }
            }
        } else {
            button.setBackgroundResource(R.drawable.custom_tab_disabled)
            button.setTextColor(getColor(R.color.font_light_gray))
            when (position) {
                1 -> {
                    button.setCompoundDrawablesWithIntrinsicBounds(speechDisable, null,null,null)
                }
                2 -> {
                    button.setCompoundDrawablesWithIntrinsicBounds(emotionDisable, null,null,null)
                }
                3 -> {
                    button.setCompoundDrawablesWithIntrinsicBounds(postureDisable, null,null,null)
                }
                else -> {
                    button.setCompoundDrawablesWithIntrinsicBounds(speechDisable, null,null,null)
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        fun newInstance() = GuideBaseFragment()
    }
}