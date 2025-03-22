package com.smart.note.dagger2

import android.util.Log
import com.smart.note.App
import com.smart.note.net.ApiService
import com.smart.note.room.AppDatabase
import com.smart.note.room.MemoDao
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(val app: App) {

    @Provides
    fun provideOkhttpClient(): OkHttpClient {
        return OkHttpClient()
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
    fun provideBaseUrl1(): String {
        Log.i("provideRetrofit", "provideBaseUrl1 ==== base url")
        return "http://baidu.com"
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