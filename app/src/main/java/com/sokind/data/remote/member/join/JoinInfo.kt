package com.sokind.data.remote.member.join

data class JoinInfo(
    val memberName: String,
    val memberEmail: String,
    val memberId: String,
    val memberPw: String,
    val memberGender: Int,
    val enterpriseKey: Int,
    val storeKey: Int,
    val positionKey: Int,
    val eventYN: Int,
)
