package com.sokind.ui.report.detail.tabs.expression

import android.graphics.Color
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.di.GlideApp
import com.sokind.data.remote.report.ReportEmotion
import com.sokind.databinding.FragmentExpressionBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import com.sokind.util.dialog.BottomSheetExplainDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ExpressionFragment(
    private val emotionData: ReportEmotion,
    private val name: String
) : BaseFragment<FragmentExpressionBinding>(R.layout.fragment_expression) {
    private val viewModel by viewModels<ExpressionViewModel>()

    override fun init() {
        setBinding()
        setBarChart()
        setGazeChart()
    }

    private fun setBinding() {
        binding.apply {
            tvExpressionName.text = getString(R.string.expression_txt_1, name)
            tvMainExpression.text = emotionData.bestEmotion
            tvViewPointName.text = getString(R.string.view_point_txt_1, name)
            tvViewPoint.text = emotionData.mostGaze

            GlideApp.with(requireContext()).load(emotionData.gazeImg).into(ivGaze)

            tvViewTitle
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val dialog =
                        BottomSheetExplainDialog.newInstance(Constants.EMOTION_DIALOG, null)
                    dialog.show(parentFragmentManager, dialog.tag)
                }, { it.printStackTrace() })
        }
    }

    private fun setBarChart() {
        binding.apply {
            chartEmotion.isClickable = false
            chartEmotion.description.isEnabled = false
            chartEmotion.setBackgroundColor(Color.WHITE)
            chartEmotion.setDrawGridBackground(false)
            chartEmotion.setDrawBarShadow(false)
            chartEmotion.isHighlightFullBarEnabled = false

            chartEmotion.setScaleEnabled(false)

            val leftAxis = chartEmotion.axisLeft
            leftAxis.setLabelCount(6, false)
            leftAxis.textColor = getColor(R.color.font_gray)
            leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART)
            leftAxis.spaceTop = 5f
            leftAxis.axisMaximum = 100f
            leftAxis.axisMinimum = 0f

            val rightAxis = chartEmotion.axisRight
            rightAxis.isEnabled = false

            val xAxis: XAxis = chartEmotion.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.axisMinimum = -0.5f
            xAxis.mAxisMaximum = 100.5f
            xAxis.setDrawGridLines(false)
            xAxis.textColor = getColor(R.color.font_gray)
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return emotion[(value % emotion.size).toInt()]
                }
            }

            val l = chartEmotion.legend
            l.isEnabled = false

            chartEmotion.data = generateBarData()
        }
    }

    private fun generateBarData(): BarData {
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, emotionData.emotionList[0].toFloat()))
        entries.add(BarEntry(1f, emotionData.emotionList[1].toFloat()))
        entries.add(BarEntry(2f, emotionData.emotionList[2].toFloat()))
        entries.add(BarEntry(3f, emotionData.emotionList[3].toFloat()))
        entries.add(BarEntry(4f, emotionData.emotionList[4].toFloat()))
        entries.add(BarEntry(5f, emotionData.emotionList[5].toFloat()))
        val set = BarDataSet(entries, "")
        set.color = getColor(R.color.main_color)
        set.valueTextColor = getColor(R.color.font_gray)
        set.valueTextSize = 0f
        set.axisDependency = YAxis.AxisDependency.LEFT
        val groupSpace = 0.06f
        val barSpace = 0.02f // x2 dataset
        val barWidth = 0.45f // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"
        val d = BarData(set)
        d.barWidth = barWidth

        // make this BarData object grouped
//        d.groupBars(0, groupSpace, barSpace); // start at x = 0
        return d
    }

    private fun setGazeChart() {
        binding.apply {
            for (i in emotionData.gazeList.indices) {
                val alpha = (emotionData.gazeList[i] * 1.5).toFloat()
                when (i) {
                    0 -> chartGaze.n1.alpha = alpha
                    1 -> chartGaze.n2.alpha = alpha
                    2 -> chartGaze.n3.alpha = alpha
                    3 -> chartGaze.n4.alpha = alpha
                    4 -> chartGaze.n5.alpha = alpha
                    5 -> chartGaze.n6.alpha = alpha
                    6 -> chartGaze.n7.alpha = alpha
                    7 -> chartGaze.n8.alpha = alpha
                    8 -> chartGaze.n9.alpha = alpha
                }
            }
        }
    }

    companion object {
        val emotion = arrayOf(
            "놀람", "두려움", "밝음", "공감", "흥분", "딱딱함"
        )
    }
}