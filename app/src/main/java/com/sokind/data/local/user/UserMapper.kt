package com.sokind.data.local.user

import com.sokind.data.remote.member.MemberInfo

object UserMapper {
    fun mappingRemoteDataToLocal(
        memberInfo: MemberInfo,
        access: String
    ): UserEntity {
        return UserEntity(
            memberInfo.memberKey,
            memberInfo.memberId,
            memberInfo.memberName,
            memberInfo.memberEmail,
            memberInfo.memberGender,
            memberInfo.memberStatus,

            memberInfo.enterpriseKey,
            memberInfo.enterpriseName,
            memberInfo.enterpriseLogo,

            memberInfo.storeKey,
            memberInfo.storeName,

            memberInfo.positionKey,
            memberInfo.positionName,

            memberInfo.availableYN,
            memberInfo.eventYN,
            memberInfo.emailYN,
            memberInfo.pushYN,

            access
        )
    }
}