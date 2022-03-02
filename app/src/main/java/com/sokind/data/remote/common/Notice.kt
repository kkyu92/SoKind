package com.sokind.data.remote.common

import com.google.gson.annotations.SerializedName

data class Notice(
    @SerializedName("noticeId")
    val id: Int,
    @SerializedName("noticeTitle")
    val title: String,
    @SerializedName("noticeContents")
    val contents: String,
    val createdAt: String,
    var isExpanded: Boolean = false
)

data class NoticeResponse(
    @SerializedName("noticelst")
    val noticeList: List<Notice>
)
