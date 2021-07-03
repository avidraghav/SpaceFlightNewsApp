package com.example.spaceflightnewsapp.utils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class Interceptor {
    companion object{

        val interceptor by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        }


    }
}