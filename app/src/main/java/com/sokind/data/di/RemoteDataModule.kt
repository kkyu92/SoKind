package com.sokind.data.di

import com.sokind.data.remote.member.MemberDataSource
import com.sokind.data.remote.member.MemberDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RemoteDataModule {
    @Binds
    abstract fun bindMemberDataSource(memberDataSourceImpl: MemberDataSourceImpl): MemberDataSource
//
//    @Binds
//    abstract fun bindSearchDataSource(searchDataSourceImpl: SearchDataSourceImpl) : SearchDataSource
//
//    @Binds
//    abstract fun bindHomeDataSource(homeDataSourceImpl: HomeDataSourceImpl) : HomeDataSource
}