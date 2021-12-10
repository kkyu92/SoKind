package com.sokind.data.di

import com.sokind.data.repository.test.TestRepository
import com.sokind.ui.join.first.JoinFirstViewModel
import com.sokind.ui.join.second.JoinSecondViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object ViewModelModule {
    @Provides
    fun provideJoinFirstViewModel(repository: TestRepository): JoinFirstViewModel {
        return JoinFirstViewModel(repository)
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