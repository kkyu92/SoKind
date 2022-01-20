package com.sokind.data.local.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    val memberKey: Int?,
    val memberId: String?,
    val memberName: String?,
    val memberEmail: String?,
    val memberGender: String?,
    val memberStatus: String?,

    val enterpriseKey: Int?,
    val enterpriseName: String?,
    val enterpriseLogo: String?,

    val storeKey: Int?,
    val storeName: String?,

    val positionKey: Int?,
    val positionName: String?,

    val availableYN: Int?,
    val eventYN:Int?,
    val emailYN:Int?,
    val pushYN:Int?,

    val access: String
) {
    @PrimaryKey
    var id: Int = 0
}