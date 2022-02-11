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