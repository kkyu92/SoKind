package com.sokind.data.remote.edu

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EduList(
    val eduDate: Int,
    @SerializedName("basicCS")
    val baseCs: List<BaseEdu>,
    @SerializedName("advancedCS")
    val deepCs: List<DeepEdu>
)

abstract class Edu : Serializable {
    abstract val type: Int
    abstract val key: Int
    abstract val title: String
    abstract val status: Int
}

data class BaseEdu(
    @SerializedName("eduType")
    override val type: Int,
    @SerializedName("eduKey")
    override val key: Int,
    @SerializedName("eduTitle")
    override val title: String,
    @SerializedName("eduProceedType")
    override val status: Int,
) : Edu()

data class DeepEdu(
    @SerializedName("eduType")
    override val type: Int,
    @SerializedName("eduKey")
    override val key: Int,
    @SerializedName("eduTitle")
    override val title: String,
    val subTitle: String,
    @SerializedName("eduThumbnail")
    val thumbnail: String,
    @SerializedName("eduProceedType")
    override val status: Int,
) : Edu()

data class StartEdu(
    @SerializedName("eduSituation")
    val title: String,
    @SerializedName("eduContents")
    val contents: String,
    val position: Int,
    val runningTime: Int
) : Serializable

data class NextEdu(
    @SerializedName("eduKey")
    val key: Int,
    @SerializedName("eduType")
    val type: Int,
    val position: Int,
    @SerializedName("eduTitle")
    val title: String,
    @SerializedName("eduProceedType")
    val status: Int,
    val analysisResult: String,
    @SerializedName("nextcsResult")
    val nextCsResult: String
) : Serializable