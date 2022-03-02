package com.sokind.data.remote.report

import com.google.gson.annotations.SerializedName

data class ReportResponse(
    @SerializedName("eduLevel")
    val lv: Int,
    @SerializedName("proceedRatio")
    val lvRatio: Double,
    @SerializedName("dayOfTraining")
    val eduDate: Int,

    @SerializedName("speakScore")
    val speak: Int,
    @SerializedName("emotionScore")
    val emotion: Int,
    @SerializedName("gestureScore")
    val posture: Int,
    @SerializedName("resultMent")
    val qualityComment: String,

    @SerializedName("overallAvg")
    val allAvg: Int,
    @SerializedName("indvAvg")
    val mAvg: Int,
    @SerializedName("indvRatio")
    val mPer: Double,
    @SerializedName("analyzingCnt")
    val analysisCnt: Int,
    @SerializedName("completeLst")
    val reportList: List<ReportItem>
)

data class ReportItem(
    @SerializedName("eduType")
    val type: Int,
    @SerializedName("eduKey")
    val key: Int,
    val enterpriseKey: Int,
    @SerializedName("eduTitle")
    val title: String,
    @SerializedName("subTitle")
    val subTitle: String,
    @SerializedName("eduProceedType")
    val status: Int,
    @SerializedName("totalScore")
    val score: Int
)

data class ReportDetail(
    @SerializedName("eduLevel")
    val lv: Int,
    @SerializedName("proceedRatio")
    val lvRatio: Double,
    @SerializedName("eduTag")
    val tag: String,
    @SerializedName("eduTitle")
    val title: String,
    @SerializedName("eduDate")
    val date: String,
    @SerializedName("commonDetail")
    val totalDetail: ReportTotal,
    val speakDetail: ReportSpeak,
    val emotionDetail: ReportEmotion,
    @SerializedName("gestureDetail")
    val postureDetail: ReportPosture
)

data class ReportTotal(
    val totalScore: Int,
    @SerializedName("allMemberAvg")
    val avgScore: Int,
    @SerializedName("indvRatio")
    val per: Int,
    val speakScore: Int,
    val emotionScore: Int,
    @SerializedName("gestureScore")
    val postureScore: Int,
    @SerializedName("recordVideo")
    val recordFile: String,
)

data class ReportSpeak(
    @SerializedName("eduMent")
    val comment: String,
    @SerializedName("hzScore")
    val matchRate: Int,
    @SerializedName("adminHzArr")
    val adminHz: List<List<Float>>,
    @SerializedName("userHzArr")
    val userHz: List<List<Float>>,
    val recordFile: String,
    @SerializedName("voiceTone")
    val tone: String,
    @SerializedName("speechRate")
    val speed: Int,

    val analysisMent: String,
    @SerializedName("answerScore")
    val complainScore: String,
    val sentimentScore: Double,
)

data class ReportEmotion(
    @SerializedName("emotionArr")
    val emotionList: List<Double>,
    val bestEmotion: String,
    @SerializedName("gazeArr")
    val gazeList: List<Double>,
    val mostGaze: String,
    val gazeImg: String
)

data class ReportPosture(
    @SerializedName("headRollCnt")
    val headRotate: Int,
    @SerializedName("headPitchCnt")
    val headShake: Int,
    @SerializedName("headYawCnt")
    val headTurn: Int,
    @SerializedName("shoulderUpdownCnt")
    val shoulderShake: Int,
    @SerializedName("shoulderLFCnt")
    val shoulderTurn: Int
)