package com.sokind.data.remote.member.join

import com.google.gson.annotations.SerializedName

data class EnterpriseList(
    val data: List<Enterprise>
)

data class EnterpriseInfo(
    val enterpriseCode: String,
    val enterpriseName: String,
    val enterpriseLogo: String,
    @SerializedName("storeinfo")
    val storeInfo: List<Store>,
    @SerializedName("positioninfo")
    val positionInfo: List<Position>
)

data class Enterprise(
    val enterpriseKey: Int,
    val enterpriseCode: String,
    val enterpriseName: String,
)

data class Position(
    val positionKey: Int,
    val positionName: String
)


data class Store(
    val storeKey: Int,
    val storeName: String,
    val storeAddress: String,
)