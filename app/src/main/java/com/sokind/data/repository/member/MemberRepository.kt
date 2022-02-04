package com.sokind.data.repository.member

import com.sokind.data.remote.member.MemberInfo
import com.sokind.data.remote.member.join.EmailResponse
import com.sokind.data.remote.member.join.EnterpriseInfo
import com.sokind.data.remote.member.join.EnterpriseList
import com.sokind.data.remote.member.join.JoinInfo
import com.sokind.data.remote.member.login.LoginRequest
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface MemberRepository {
    fun searchEnterpriseList(keyword: String): Single<EnterpriseList>
    fun getEnterpriseInfo(code: String, key: Int): Single<EnterpriseInfo>
    fun sendEmail(email: String): Single<EmailResponse>
    fun checkId(id: String): Completable
    fun signUp(joinInfo: JoinInfo): Completable
//    fun getStoreInfo(enterpriseKey:String, storeKey:String): Single<StoreList>
//    fun getPositionInfo(enterpriseKey: String): Single<PositionList>
    fun login(loginRequest: LoginRequest): Completable
    fun getMe(): Single<MemberInfo>
    fun saveUser(memberInfo: MemberInfo): Completable
    fun isLogin(): Single<String>
}