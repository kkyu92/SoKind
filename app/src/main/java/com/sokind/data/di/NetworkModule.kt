package com.sokind.data.di

import com.sokind.BuildConfig
import com.sokind.data.remote.edu.EduApi
import com.sokind.data.remote.guide.GuideApi
import com.sokind.data.remote.member.MemberApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideHttpLoggerInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    @Named("base_factory")
    fun provideCallFactory(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): Call.Factory {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRxJava3CallAdapterFactory(): RxJava3CallAdapterFactory {
        return RxJava3CallAdapterFactory.create()
    }

    @Singleton
    @Provides
    @Named("base_url")
    fun provideBaseUrl(): String {
        return BuildConfig.BASE_URL
    }

    @Singleton
    @Provides
    @Named("base_retrofit")
    fun provideRetrofit(
        @Named("base_factory") httpLoggingInterceptor: Call.Factory,
        gsonConverterFactory: GsonConverterFactory,
        rxJava3CallAdapterFactory: RxJava3CallAdapterFactory,
        @Named("base_url") baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .callFactory(httpLoggingInterceptor)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJava3CallAdapterFactory)
            .baseUrl(baseUrl)
            .build()
    }

    @Singleton
    @Provides
    fun provideLoginApi(@Named("base_retrofit") retrofit: Retrofit): MemberApi {
        return retrofit.create(MemberApi::class.java)
    }

    @Singleton
    @Provides
    fun provideEduApi(@Named("base_retrofit") retrofit: Retrofit): EduApi {
        return retrofit.create(EduApi::class.java)
    }

    @Singleton
    @Provides
    fun provideGuideApi(@Named("base_retrofit") retrofit: Retrofit): GuideApi {
        return retrofit.create(GuideApi::class.java)
    }
//
//    @Singleton
//    @Provides
//    fun provideSearchApi(@Named("base_retrofit") retrofit: Retrofit): SearchApi {
//        return retrofit.create(SearchApi::class.java)
//    }

    ///////////////////////////////--Test
//    @Singleton
//    @Provides
//    @Named("test_url")
//    fun provideTestUrl(): String {
//        return BuildConfig.TEST_URL
//    }
//
//    @Singleton
//    @Provides
//    @Named("test_api_key")
//    fun provideTestApiKey(): String {
//        return BuildConfig.TEST_API_KEY
//    }

//    @Singleton
//    @Provides
//    fun provideTestApiKeyInterceptor(@Named("test_api_key") apiKey: String): Interceptor {
//        return Interceptor.invoke { chain ->
//            val originalRequest = chain.request()
//            val originalUrl = originalRequest.url
//
//            val newUrl = originalUrl.newBuilder()
//                .addQueryParameter("api_key", apiKey)
//                .build()
//            val newRequest = originalRequest.newBuilder()
//                .url(newUrl)
//                .build()
//
//            chain.proceed(newRequest)
//        }
//    }
//
//    @Singleton
//    @Provides
//    @Named("test_factory")
//    fun provideCallTestFactory(
//        httpLoggingInterceptor: HttpLoggingInterceptor,
//        testApiKeyInterceptor: Interceptor
//    ): Call.Factory {
//        return OkHttpClient.Builder()
//            .addInterceptor(httpLoggingInterceptor)
//            .addInterceptor(testApiKeyInterceptor)
//            .build()
//    }
//
//    @Singleton
//    @Provides
//    @Named("test_retrofit")
//    fun provideTestRetrofit(
//        @Named("test_factory") httpLoggingInterceptor: Call.Factory,
//        gsonConverterFactory: GsonConverterFactory,
//        rxJava3CallAdapterFactory: RxJava3CallAdapterFactory,
//        @Named("test_url") baseUrl: String
//    ): Retrofit {
//        return Retrofit.Builder()
//            .callFactory(httpLoggingInterceptor)
//            .addConverterFactory(gsonConverterFactory)
//            .addCallAdapterFactory(rxJava3CallAdapterFactory)
//            .baseUrl(baseUrl)
//            .build()
//    }
//
//    @Singleton
//    @Provides
//    fun provideTestApi(@Named("test_retrofit") retrofit: Retrofit): TestApi {
//        return retrofit.create(TestApi::class.java)
//    }
}