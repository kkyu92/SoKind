package com.sokind.data.remote.member

import com.sokind.data.remote.member.join.EmailResponse
import com.sokind.data.remote.member.join.EnterpriseInfo
import com.sokind.data.remote.member.join.EnterpriseList
import com.sokind.data.remote.member.join.JoinInfo
import com.sokind.data.remote.member.login.LoginRequest
import com.sokind.data.remote.member.login.LoginResponse
import com.sokind.data.remote.member.login.RefreshRequest
import com.sokind.data.remote.member.login.RefreshResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface MemberDataSource {
    fun searchEnterpriseList(keyword: String): Single<EnterpriseList>
    fun getEnterpriseInfo(code: String, key: Int): Single<EnterpriseInfo>
    fun sendEmail(email: String): Single<EmailResponse>
    fun checkId(id: String): Completable
    fun signUp(joinInfo: JoinInfo): Completable
//    fun getStoreInfo(enterpriseKey:String, storeKey:String): Single<StoreList>
//    fun getPositionInfo(enterpriseKey: String): Single<PositionList>
    fun login(loginRequest: LoginRequest): Single<LoginResponse>
    fun refreshToken(access: String, request: RefreshRequest): Single<RefreshResponse>
    fun checkToken(access: String, id: String): Completable
    fun getMe(access: String, id: String): Single<MemberInfo>
}