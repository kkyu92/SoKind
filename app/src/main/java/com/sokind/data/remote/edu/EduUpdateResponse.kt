package com.sokind.data.remote.edu

class EduUpdateResponse(
    val eduType: Int,
    val eduKey: Int,
    val enterpriseKey: Int,
    val eduTitle: String,
    val subTitle: String,
    val eduContents: String,
    val eduMent: String,
    val eduThumbnail: String,
    val analysisResult: String,
    val nextcsResult: String
)