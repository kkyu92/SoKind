package com.sokind.data.di

import com.sokind.data.remote.test.TestDataSource
import com.sokind.data.remote.test.TestDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RemoteDataModule {
    @Binds
    abstract fun bindTestDataSource(testDataSourceImpl: TestDataSourceImpl) : TestDataSource
//
//    @Binds
//    abstract fun bindSearchDataSource(searchDataSourceImpl: SearchDataSourceImpl) : SearchDataSource
//
//    @Binds
//    abstract fun bindHomeDataSource(homeDataSourceImpl: HomeDataSourceImpl) : HomeDataSource
}