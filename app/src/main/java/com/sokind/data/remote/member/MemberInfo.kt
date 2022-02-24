package com.sokind.data.remote.member

import java.io.Serializable

data class MemberInfo(
    val memberKey: Int,
    val memberId: String,
    val memberName: String,
    val profileImg: String,
    val memberEmail: String,
    val memberGender: Int,
    val memberStatus: String,

    val enterpriseKey: Int,
    val enterpriseName: String,
    val enterpriseLogo: String,

    val storeKey: Int,
    val storeName: String,

    val positionKey: Int,
    val positionName: String,

    val availableYN: Int,
    val eventYN:Int,
    val emailYN:Int,
    val pushYN:Int,
) : Serializable
