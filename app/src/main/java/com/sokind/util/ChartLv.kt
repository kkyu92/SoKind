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
            0f -> {
                comment = "쏘카인드로 서비스교육을 시작해볼까요?"
            }
            in 1f..19f -> {
                comment = "아직 쏘카인드 새내기입니다~!"
            }
            in 20f..39f -> {
                comment = "열심히 교육중이시네요. 화이팅!"
            }
            in 40f..59f -> {
                comment = "친절왕으로 달려가는 중 입니다."
            }
            in 60f..79f -> {
                comment = "거의 다 왔어요! 조금만 힘내세요!"
            }
            in 80f..99f -> {
                comment = "끝이 보여요! 조금만 힘내세요!"
            }
            100f -> {
                comment = "교육 끝~! 수고 많으셨습니다."
            }
            else -> {
                comment = "쏘카인드로 서비스교육을 시작해볼까요?"
            }
        }

        binding.apply {
            tvLvComment.text = comment
            tvLvPercent.text = progress.toInt().toString() + "%"
            pbLv.progress = progress.toInt()
        }
    }
}