package com.sokind.data.remote.member.info

data class EmailRequest(
    val memberId: String,
    val memberEmail: String,
    val newEmail: String
)