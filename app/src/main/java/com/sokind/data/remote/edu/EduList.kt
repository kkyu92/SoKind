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

data class Edu(
    @SerializedName("eduType")
    val type: Int,
    @SerializedName("eduKey")
    val key: Int,
    val enterpriseKey: Int,
    val position: Int,
    @SerializedName("eduTitle")
    val title: String,
    val subTitle: String,
    @SerializedName("eduContents")
    val contents: String,
    @SerializedName("eduMent")
    val ment: String,
    val recordFile: String,
    @SerializedName("eduThumbnail")
    val thumbnail: String,
    val runningTime: Int,
    @SerializedName("eduProceedType")
    val status: Int,
) : Serializable
