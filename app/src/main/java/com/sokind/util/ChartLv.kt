package com.sokind.util

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
        val progress = fin / total * 100
        val comment: String
        when(progress) {
            in 0f..20f -> {
                comment = "0~20"
            }
            in 21f..40f -> {
                comment = "21~40"
            }
            in 41f..60f -> {
                comment = "41~60"
            }
            in 61f..80f -> {
                comment = "61~80"
            }
            in 81f..100f -> {
                comment = "거의 다 왔어요! 조금만 힘내세요!"
            }
            else -> {
                comment = "else"
            }
        }

        binding.apply {
            tvLvComment.text = comment
            tvLvPercent.text = progress.toInt().toString() + "%"
            pbLv.progress = progress.toInt()
        }
    }
}