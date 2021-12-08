package com.sokind.data.di

import com.sokind.data.local.user.UserDataSource
import com.sokind.data.local.user.UserDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class LocalDataModule {
    @Binds
    abstract fun bindTokenDataSource(tokenDataSourceImpl: UserDataSourceImpl): UserDataSource

//    @Binds
//    abstract fun bindRecentSearchDataSource(recentSearchDataSourceImpl: RecentSearchDataSourceImpl): RecentSearchDataSource
}