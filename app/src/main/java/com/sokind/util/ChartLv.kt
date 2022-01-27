package com.sokind.util

import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.constraintlayout.widget.ConstraintLayout
import com.sokind.data.remote.edu.EduList
import com.sokind.databinding.ChartLvBinding

class ChartLv(
    binding: ChartLvBinding,
    eduAll: EduList
) {
    init {
        val baseList = eduAll.baseCs
        val deepList = eduAll.deepCs
        val total = (baseList.size + deepList.size).toFloat()
        var fin = 0f

        for (base in baseList) {
            if (base.status == 1) {
                fin++
            }
        }
        for (deep in deepList) {
            if (deep.status == 1) {
                fin++
            }
        }

        val ani: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                val lv = binding.lvSelected.layoutParams as ConstraintLayout.LayoutParams
                lv.horizontalBias = fin / total
                binding.lvSelected.layoutParams = lv
            }
        }
        binding.lvSelected.startAnimation(ani)
    }
}