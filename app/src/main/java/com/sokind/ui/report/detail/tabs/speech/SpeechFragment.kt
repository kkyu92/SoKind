package com.sokind.ui.report.detail.tabs.speech

import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sokind.R
import com.sokind.databinding.FragmentSpeechBinding
import com.sokind.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpeechFragment : BaseFragment<FragmentSpeechBinding>(R.layout.fragment_speech) {
    private val viewModel by viewModels<SpeechViewModel>()

    override fun init() {
        setBinding()
        setLineChart()
    }

    private fun setBinding() {

    }

    private fun setLineChart() {
        val data =
            "[[2, 0.27396], [6, 0.29933], [10, 0.23042], [12, 0.09934], [15,0.22333], [19, 0.4872], [21, 0.09775], [23, 0.10191], [25, 0.11062], [29, 0.45057], [31, 0.60472], [38, 0.40108], [44, 0.55048], [47, 0.27526], [56, 0.66702], [62, 0.49985], [68, 0.49257], [70, 0.49285], [75, 0.21609], [82, 0.63693], [88, 0.86348], [95, 0.79862], [105, 0.09594], [110, 0.09585], [112, 0.09527], [115, 0.09611], [119, 0.0964], [123, 0.09649], [128, 0.34806], [131, 0.0977], [134, 0.09765]]"
        val data2 =
            "[[17, 0.10912], [20, 0.10576], [26, 0.71714], [29, 0.58808], [33, 0.81504], [36, 0.5578], [39, 0.28532], [41, 0.56998], [45, 0.56749], [50, 0.84044], [54, 0.55812], [65, 0.79803], [70, 0.2776], [74, 0.27213], [78, 0.52504], [80, 0.75494], [93, 0.75608], [98, 0.77977], [102, 0.7639], [105, 0.10894], [111, 0.20396], [113, 0.3606], [115, 0.09436], [117, 0.09363], [123, 0.21819], [127, 0.30974], [130, 0.3107], [132, 0.36223], [137, 0.10986], [141, 0.22711], [146, 0.309]]"

        val gson = Gson()
        val list = gson.fromJson<MutableList<MutableList<Float>>>(
            data,
            object : TypeToken<MutableList<MutableList<Float>>>() {}.type
        )
        val list2 = gson.fromJson<MutableList<MutableList<Float>>>(
            data2,
            object : TypeToken<MutableList<MutableList<Float>>>() {}.type
        )

        val mList: ArrayList<Entry> = arrayListOf()
        val defaultList: ArrayList<Entry> = arrayListOf()

        for (i in list.indices) {
            val entry = Entry(list[i][0], list[i][1])
            mList.add(entry)
        }
        for (i in list2.indices) {
            val entry = Entry(list2[i][0], list2[i][1])
            defaultList.add(entry)
        }

        val mDataSet = LineDataSet(mList, "내 목소리")
        val defaultDataSet = LineDataSet(defaultList, "CS매뉴얼")

        defaultDataSet.apply {
            color = resources.getColor(R.color.sub_color1)
            lineWidth = 2f
            fillColor = resources.getColor(R.color.sub_color1)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawValues(true)
            setDrawCircles(false)
            valueTextSize = 0f
        }
        mDataSet.apply {
            color = resources.getColor(R.color.main_color)
            lineWidth = 2f
            fillColor = resources.getColor(R.color.main_color)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawValues(true)
            setDrawCircles(false)
            valueTextSize = 0f
        }

        val lineData = LineData()
        lineData.addDataSet(defaultDataSet)
        lineData.addDataSet(mDataSet)

        binding.lineChart.apply {
            xAxis.setDrawLabels(false)
            xAxis.setDrawGridLines(false)
            xAxis.axisMinimum = 0f
            xAxis.position = XAxis.XAxisPosition.BOTTOM

            axisRight.isEnabled = false
            axisLeft.setDrawLabels(false)
            axisLeft.setDrawGridLines(false)
            axisLeft.axisMinimum = 0f
            axisLeft.axisMaximum = 1f

            description.isEnabled = false
            setScaleEnabled(false)
            legend.apply {
                textSize = 12f
                textColor = resources.getColor(R.color.font_light_gray)
            }
        }.data = lineData
    }

    companion object {
        fun newInstance() = SpeechFragment()
    }
}