package com.sokind.data.remote.guide

import com.google.gson.annotations.SerializedName

data class ManualResponse(
    @SerializedName("CSguide")
    val manualList: List<Manual>
)
data class Manual(
    val position: Int,
    @SerializedName("eduTitle")
    val title: String,
    @SerializedName("guideContents")
    val content: String,
    var isExpanded: Boolean = false
)