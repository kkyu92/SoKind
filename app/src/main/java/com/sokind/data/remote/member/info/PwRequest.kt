package com.sokind.data.remote.member.info

data class PwRequest(
    val changePw: String,
    val memberId: String,
    val memberKey: Int,
    val memberPw: String,
)
