package com.sokind.data.remote.member.login

data class RefreshResponse(
    val resultCode: String,
    val issueMessage: String,
    val accessToken: String
)
