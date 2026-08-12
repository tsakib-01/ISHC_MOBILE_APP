package com.example.data.remote

import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val DEFAULT_BASE_URL = "https://ishc-backend-production-api.run.app/"

    var authToken: String? = null

    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val builder = original.newBuilder()
            .header("Accept", "application/json")
        
        authToken?.let { token ->
            builder.header("Authorization", "Bearer $token")
        }
        
        chain.proceed(builder.build())
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    val apiService: IshcApiService by lazy {
        val baseUrl = try {
            val configUrl = BuildConfig::class.java.getField("BACKEND_API_URL").get(null) as? String
            if (!configUrl.isNull_or_Empty()) configUrl else DEFAULT_BASE_URL
        } catch (e: Exception) {
            DEFAULT_BASE_URL
        }

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(IshcApiService::class.java)
    }

    private fun String?.isNull_or_Empty(): Boolean = this == null || this.trim().isEmpty()
}
