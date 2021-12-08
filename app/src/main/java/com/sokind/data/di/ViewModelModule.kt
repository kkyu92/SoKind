package com.sokind.data.di

import com.sokind.data.repository.test.TestRepository
import com.sokind.data.repository.test.TestRepositoryImpl
import com.sokind.ui.join.JoinViewModel
import com.sokind.ui.login.LoginViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object ViewModelModule {
    @Provides
    fun provideLoginViewModel(repository: TestRepository): JoinViewModel {
        return JoinViewModel(repository)
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