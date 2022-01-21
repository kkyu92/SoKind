package com.sokind.data.remote.edu

import com.google.gson.annotations.SerializedName

data class EduList(
    @SerializedName("basicCS")
    val baseCs: List<Edu>,
    @SerializedName("advancedCS")
    val deepCs: List<Edu>
)

data class Edu(
    val enterpriseKey: Int,
    @SerializedName("eduType")
    val type: Int,
    @SerializedName("eduKey")
    val key: Int,
    @SerializedName("eduTitle")
    val title: String,
    @SerializedName("eduContents")
    val contents: String,
    @SerializedName("eduThumbnail")
    val thumbnail: String,
    @SerializedName("eduProceedType")
    val status: Int,
    val runningTime: Int,
)
