package com.sokind.data.di

import com.sokind.data.repository.base.BaseRepository
import com.sokind.data.repository.base.BaseRepositoryImpl
import com.sokind.data.repository.edu.EduRepository
import com.sokind.data.repository.edu.EduRepositoryImpl
import com.sokind.data.repository.member.MemberRepository
import com.sokind.data.repository.member.MemberRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsBaseRepository(
        repositoryImpl: BaseRepositoryImpl
    ): BaseRepository

    @Binds
    abstract fun bindsMemberRepository(
        repositoryImpl: MemberRepositoryImpl
    ): MemberRepository

    @Binds
    abstract fun bindsEduRepository(
        repositoryImpl: EduRepositoryImpl
    ): EduRepository

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