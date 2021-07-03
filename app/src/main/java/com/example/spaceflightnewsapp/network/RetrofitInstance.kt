package com.example.spaceflightnewsapp.network

import com.example.spaceflightnewsapp.utils.Constants.Companion.BASE_URL_LAUNCHLIBRARY
import com.example.spaceflightnewsapp.utils.Constants.Companion.BASE_URL_SPACEFLIGHT
import com.example.spaceflightnewsapp.utils.Interceptor.Companion.interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        val logging = HttpLoggingInterceptor()
        val level = logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(level)
            .build()

        private val retrofit_spaceflight by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL_SPACEFLIGHT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        private val retrofit_launchlibrary by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL_LAUNCHLIBRARY)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        // this object will be used to make network requests
        val api_spaceflight by lazy {
            retrofit_spaceflight.create(SpaceFlightAPI::class.java)
        }
        val api_launchlibrary by lazy {
            retrofit_launchlibrary.create(LaunchLibrary::class.java)
        }


    }



}