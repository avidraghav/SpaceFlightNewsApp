package com.example.spaceflightnewsapp.network

import com.example.spaceflightnewsapp.models.ArticlesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SpaceFlightAPI {

    @GET("/articles")
    suspend fun getArticles(
        @Query("_start")
        articlesToSkip : Int = 0
    ): Response<ArticlesResponse>

    @GET("/articles")
    suspend fun searchArticles(
        @Query("_title_contains")
        searchQuery : String
    ): Response<ArticlesResponse>

}