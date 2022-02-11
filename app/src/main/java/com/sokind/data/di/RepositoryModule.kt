package com.sokind.data.di

import com.sokind.data.repository.edu.EduRepository
import com.sokind.data.repository.edu.EduRepositoryImpl
import com.sokind.data.repository.guide.GuideRepository
import com.sokind.data.repository.guide.GuideRepositoryImpl
import com.sokind.data.repository.member.MemberRepository
import com.sokind.data.repository.member.MemberRepositoryImpl
import com.sokind.data.repository.report.ReportRepository
import com.sokind.data.repository.report.ReportRepositoryImpl
import com.sokind.data.repository.token.TokenRepository
import com.sokind.data.repository.token.TokenRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsTokenRepository(
        repositoryImpl: TokenRepositoryImpl
    ): TokenRepository

    @Binds
    abstract fun bindsMemberRepository(
        repositoryImpl: MemberRepositoryImpl
    ): MemberRepository

    @Binds
    abstract fun bindsEduRepository(
        repositoryImpl: EduRepositoryImpl
    ): EduRepository

    @Binds
    abstract fun bindsGuideRepository(
        repositoryImpl: GuideRepositoryImpl
    ): GuideRepository

    @Binds
    abstract fun bindsReportRepository(
        repositoryImpl: ReportRepositoryImpl
    ): ReportRepository
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