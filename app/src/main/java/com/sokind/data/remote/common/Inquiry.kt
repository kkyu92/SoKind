package com.sokind.data.remote.common

import com.google.gson.annotations.SerializedName

data class Inquiry(
    @SerializedName("questionId")
    val id: Int,
    @SerializedName("questionType")
    val type: String,
    @SerializedName("questionTitle")
    val title: String,
    @SerializedName("questionContents")
    val contents: String,
    @SerializedName("questionAnswer")
    val answer: String,
    val createdAt: String,
    val replyFlag: Int,
    var isExpanded: Boolean = false
)

data class InquiryRequest(
    @SerializedName("memberId")
    val userId: String,
    @SerializedName("questionType")
    val type: String,
    @SerializedName("questionTitle")
    val title: String,
    @SerializedName("questionContents")
    val contents: String,
)

data class InquiryResponse(
    @SerializedName("questions")
    val inquiryList: List<Inquiry>
)
