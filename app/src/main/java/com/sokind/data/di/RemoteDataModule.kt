package com.sokind.data.di

import com.sokind.data.remote.edu.EduDataSource
import com.sokind.data.remote.edu.EduDataSourceImpl
import com.sokind.data.remote.guide.GuideDataSource
import com.sokind.data.remote.guide.GuideDataSourceImpl
import com.sokind.data.remote.member.MemberDataSource
import com.sokind.data.remote.member.MemberDataSourceImpl
import com.sokind.data.remote.report.ReportDataSource
import com.sokind.data.remote.report.ReportDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RemoteDataModule {
    @Binds
    abstract fun bindMemberDataSource(memberDataSourceImpl: MemberDataSourceImpl): MemberDataSource

    @Binds
    abstract fun bindEduDataSource(eduDataSourceImpl: EduDataSourceImpl): EduDataSource

    @Binds
    abstract fun bindGuideDataSource(guideDataSourceImpl: GuideDataSourceImpl): GuideDataSource

    @Binds
    abstract fun bindReportDataSource(reportDataSourceImpl: ReportDataSourceImpl): ReportDataSource
//
//    @Binds
//    abstract fun bindSearchDataSource(searchDataSourceImpl: SearchDataSourceImpl) : SearchDataSource
//
//    @Binds
//    abstract fun bindHomeDataSource(homeDataSourceImpl: HomeDataSourceImpl) : HomeDataSource
}