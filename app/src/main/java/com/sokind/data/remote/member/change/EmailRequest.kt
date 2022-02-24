package com.sokind.data.remote.member.change

data class EmailRequest(
    val memberId: String,
    val memberEmail: String,
    val newEmail: String
)