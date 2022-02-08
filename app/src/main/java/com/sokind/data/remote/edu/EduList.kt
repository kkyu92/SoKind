package com.sokind.data.remote.edu

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EduList(
    val eduDate: Int,
    @SerializedName("basicCS")
    val baseCs: List<Edu>,
    @SerializedName("advancedCS")
    val deepCs: List<Edu>
)

abstract class SuperEdu : Serializable {
    abstract val type: Int
    abstract val key: Int
    abstract val enterpriseKey: Int
    abstract val position: Int
    abstract val title: String
    abstract val subTitle: String
    abstract val contents: String
    abstract val ment: String
    abstract val recordFile: String
    abstract val thumbnail: String
    abstract val runningTime: Int
    abstract val status: Int
}

data class Edu(
    @SerializedName("eduType")
    override val type: Int,
    @SerializedName("eduKey")
    override val key: Int,
    override val enterpriseKey: Int,
    override val position: Int,
    @SerializedName("eduTitle")
    override val title: String,
    override val subTitle: String,
    @SerializedName("eduContents")
    override val contents: String,
    @SerializedName("eduMent")
    override val ment: String,
    override val recordFile: String,
    @SerializedName("eduThumbnail")
    override val thumbnail: String,
    override val runningTime: Int,
    @SerializedName("eduProceedType")
    override val status: Int,
) : SuperEdu()

data class NextEdu(
    @SerializedName("eduType")
    override val type: Int,
    @SerializedName("eduKey")
    override val key: Int,
    override val enterpriseKey: Int,
    override val position: Int,
    @SerializedName("eduTitle")
    override val title: String,
    override val subTitle: String,
    @SerializedName("eduContents")
    override val contents: String,
    @SerializedName("eduMent")
    override val ment: String,
    override val recordFile: String,
    @SerializedName("eduThumbnail")
    override val thumbnail: String,
    override val runningTime: Int,
    @SerializedName("eduProceedType")
    override val status: Int,
    val analysisResult: String,
    @SerializedName("nextcsResult")
    val nextCsResult: String
) : SuperEdu()
