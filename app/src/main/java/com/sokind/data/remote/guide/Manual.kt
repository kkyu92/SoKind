package com.sokind.data.remote.guide

data class Manual(
    val title: String,
    val subTitle: String,
    val content: String,
    var isExpanded: Boolean = false
)
