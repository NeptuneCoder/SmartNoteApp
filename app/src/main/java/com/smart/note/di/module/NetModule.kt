package com.smart.note.di.module

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.smart.note.BuildConfig
import com.smart.note.di.scope.NetScope
import com.smart.note.net.ApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
class NetModule {
    @NetScope
    @Named("apiKey")
    @Provides
    fun provideApiKey(): String {
        return BuildConfig.DEEPSEEK_API_KEY

    }

    @NetScope
    @Provides
    fun provideLoggerInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @NetScope
    @Provides
    fun provideOkhttpClient(
        @Named("apiKey") apiKey: String,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // 连接超时
            .readTimeout(60, TimeUnit.SECONDS)     // 读取超时（关键！）
            .writeTimeout(30, TimeUnit.SECONDS)    // 写入超时
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Authorization", "Bearer $apiKey")
                    .header("Content-Type", "application/json")
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }.addInterceptor(loggingInterceptor)
            .build()

    }

    @NetScope
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
        Log.i("provideRetrofit", "okHttpClient ==== $okHttpClient")
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create()) // 使用 Gson 解析 JSON
            .addCallAdapterFactory(CoroutineCallAdapterFactory()) // 添加适配器
            .build()
    }

    @NetScope
    @Provides
    fun provideDeepSeekUrl(): String {
        Log.i("provideRetrofit", "provideBaseUrl1 ==== base url")
        return "https://api.deepseek.com"
    }

    @NetScope
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}