package com.sokind.data.remote.member.change

data class PwRequest(
    val changePw: String,
    val memberId: String,
    val memberKey: Int,
    val memberPw: String,
)
