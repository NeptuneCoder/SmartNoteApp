package com.smart.note.dagger2

import android.util.Log
import com.smart.note.App
import com.smart.note.BuildConfig
import com.smart.note.net.ApiService
import com.smart.note.room.AppDatabase
import com.smart.note.room.MemoDao
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(val app: App) {
    @Named("apiKey")
    @Provides
    fun provideApiKey(): String {
        return BuildConfig.DEEPSEEK_API_KEY

    }

    @Provides
    fun provideLoggerInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun provideOkhttpClient(
        @Named("apiKey") apiKey: String,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
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

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
        Log.i("provideRetrofit", "okHttpClient ==== $okHttpClient")
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create()) // 使用 Gson 解析 JSON
            .build()
    }

    @Provides
    fun provideDeepSeekUrl(): String {
        Log.i("provideRetrofit", "provideBaseUrl1 ==== base url")
        return "https://api.deepseek.com"
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideMemoDao(appDatabase: AppDatabase): MemoDao {
        return appDatabase.noteDao()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(): AppDatabase {
        return AppDatabase.getDatabase(app)
    }
}