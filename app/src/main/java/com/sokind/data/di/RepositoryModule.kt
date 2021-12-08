package com.sokind.data.di

import com.sokind.data.repository.test.TestRepository
import com.sokind.data.repository.test.TestRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsTestRepository(
        repositoryImpl: TestRepositoryImpl
    ): TestRepository

//    @Binds
//    abstract fun bindsMyPageRepository(
//        repositoryImpl: MyPageRepositoryImpl
//    ): MyPageRepository
//
//    @Binds
//    abstract fun bindsHomeRepository(
//        repositoryImpl: HomeRepositoryImpl
//    ): HomeRepository
//
//    @Binds
//    abstract fun bindsSearchRepository(
//        repositoryImpl: SearchRepositoryImpl
//    ): SearchRepository
}