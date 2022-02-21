package com.sokind.ui.report.detail.tabs.speech

import android.annotation.SuppressLint
import android.net.Uri
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.remote.report.ReportSpeak
import com.sokind.databinding.FragmentSpeechBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import com.sokind.util.dialog.BottomSheetExplainDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SpeechFragment(
    private val speakData: ReportSpeak
) : BaseFragment<FragmentSpeechBinding>(R.layout.fragment_speech) {
    private val viewModel by viewModels<SpeechViewModel>()
    private lateinit var exoPlayer: ExoPlayer

    override fun init() {
        setBinding()
        setLineChart(speakData.userHz, speakData.userHz)
        setVoicePlayer()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setBinding() {
        binding.apply {
            tvMent.text = speakData.comment
            tvMatchRate.text = getString(R.string.per, speakData.matchRate.toString())
            tvTone.text = speakData.tone
            tvToneContent.text = getString(R.string.tone_content, speakData.tone)
            tvSpeedSps.text = speakData.speed.toString()

            tvSpeedTitle
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val dialog = BottomSheetExplainDialog.newInstance(
                        getString(R.string.speed_title),
                        getString(R.string.speed_content),
                        null,
                        getDrawable(R.drawable.dialog_speed)
                    )
                    dialog.show(parentFragmentManager, dialog.tag)
                }, { it.printStackTrace() })
        }
    }

    private fun setVoicePlayer() {
        binding.apply {
            exoPlayer = ExoPlayer.Builder(requireContext()).build()
            playerView.player = exoPlayer
            playerView.showTimeoutMs = 0

            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_BUFFERING -> {
                            Timber.e("STATE_BUFFERING")
                        }
                        Player.STATE_ENDED -> {
                            Timber.e("STATE_ENDED")
                        }
                        Player.STATE_IDLE -> {
                            Timber.e("STATE_IDLE")
                        }
                        Player.STATE_READY -> {
                            Timber.e("STATE_READY")
                        }
                        else -> {
                            Timber.e("ELSE")
                        }
                    }
                }
            })
            val videoSource = Uri.parse(speakData.recordFile)
            val mediaItem = MediaItem.fromUri(videoSource)

            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        }
    }

    private fun setLineChart(userList: List<List<Float>>, adminList: List<List<Float>>) {
//        val data =
//            "[[2, 0.27396], [6, 0.29933], [10, 0.23042], [12, 0.09934], [15,0.22333], [19, 0.4872], [21, 0.09775], [23, 0.10191], [25, 0.11062], [29, 0.45057], [31, 0.60472], [38, 0.40108], [44, 0.55048], [47, 0.27526], [56, 0.66702], [62, 0.49985], [68, 0.49257], [70, 0.49285], [75, 0.21609], [82, 0.63693], [88, 0.86348], [95, 0.79862], [105, 0.09594], [110, 0.09585], [112, 0.09527], [115, 0.09611], [119, 0.0964], [123, 0.09649], [128, 0.34806], [131, 0.0977], [134, 0.09765]]"
//        val data2 =
//            "[[17, 0.10912], [20, 0.10576], [26, 0.71714], [29, 0.58808], [33, 0.81504], [36, 0.5578], [39, 0.28532], [41, 0.56998], [45, 0.56749], [50, 0.84044], [54, 0.55812], [65, 0.79803], [70, 0.2776], [74, 0.27213], [78, 0.52504], [80, 0.75494], [93, 0.75608], [98, 0.77977], [102, 0.7639], [105, 0.10894], [111, 0.20396], [113, 0.3606], [115, 0.09436], [117, 0.09363], [123, 0.21819], [127, 0.30974], [130, 0.3107], [132, 0.36223], [137, 0.10986], [141, 0.22711], [146, 0.309]]"
//
//        val gson = Gson()
//        val list = gson.fromJson<MutableList<MutableList<Float>>>(
//            data,
//            object : TypeToken<MutableList<MutableList<Float>>>() {}.type
//        )
//        val list2 = gson.fromJson<MutableList<MutableList<Float>>>(
//            data2,
//            object : TypeToken<MutableList<MutableList<Float>>>() {}.type
//        )

        val mList: ArrayList<Entry> = arrayListOf()
        val defaultList: ArrayList<Entry> = arrayListOf()

        for (i in userList.indices) {
            val entry = Entry(userList[i][0], userList[i][1])
            mList.add(entry)
        }
        for (i in adminList.indices) {
            val entry = Entry(adminList[i][0], adminList[i][1])
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

    override fun onPause() {
        super.onPause()
        exoPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }
}