package com.sokind.data.di

import com.sokind.data.repository.edu.EduRepository
import com.sokind.data.repository.guide.GuideRepository
import com.sokind.data.repository.member.MemberRepository
import com.sokind.data.repository.report.ReportRepository
import com.sokind.data.repository.report.ReportRepositoryImpl
import com.sokind.ui.guide.tabs.manual.ManualViewModel
import com.sokind.ui.home.HomeViewModel
import com.sokind.ui.join.first.JoinFirstViewModel
import com.sokind.ui.login.LoginViewModel
import com.sokind.ui.my.MyViewModel
import com.sokind.ui.my.info.InfoViewModel
import com.sokind.ui.report.ReportViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object ViewModelModule {
    @Provides
    fun provideLoginViewModel(repository: MemberRepository): LoginViewModel {
        return LoginViewModel(repository)
    }

    @Provides
    fun provideJoinViewModel(repository: MemberRepository): JoinFirstViewModel {
        return JoinFirstViewModel(repository)
    }

    @Provides
    fun provideHomeModel(
        memberRepository: MemberRepository,
        eduRepository: EduRepository
    ): HomeViewModel {
        return HomeViewModel(memberRepository, eduRepository)
    }

    @Provides
    fun provideManualViewModel(repository: GuideRepository): ManualViewModel {
        return ManualViewModel(repository)
    }

    @Provides
    fun provideReportViewModel(repository: ReportRepository): ReportViewModel {
        return ReportViewModel(repository)
    }

    @Provides
    fun provideMyViewModel(repository: MemberRepository): MyViewModel {
        return MyViewModel(repository)
    }
//    @Provides
//    fun provideMyPageViewModel(repository: MyPageRepository): MyPageViewModel {
//        return MyPageViewModel(repository)
//    }
//    @Provides
//    fun provideSearchViewModel(repository: SearchRepository): SearchViewModel {
//        return SearchViewModel(repository)
//    }
//    @Provides
//    fun provideHomeViewModel(repository: HomeRepository): HomeViewModel {
//        return HomeViewModel(repository)
//    }
}