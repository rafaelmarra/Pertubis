package com.example.rafaelmarra.pertubis.model.cep

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitService {

    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .build()


    val retrofit = Retrofit.Builder()
        .baseUrl("https://viacep.com.br/ws/")
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun serviceCep() = retrofit.create(Api::class.java)
}