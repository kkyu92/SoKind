package com.sokind.data.remote.member.login

data class LoginResponse(
    val memberKey: Int,
    val memberId: String,
    val accessToken: String,
    val refreshToken: String
)