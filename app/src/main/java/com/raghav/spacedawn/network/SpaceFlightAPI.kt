package com.raghav.spacedawn.network

import com.raghav.spacedawn.models.spaceflightapi.ArticlesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SpaceFlightAPI {

    @GET("articles")
    suspend fun getArticles(
        @Query("_start")
        articlesToSkip: Int = 0
    ): Response<ArticlesResponse>

    @GET("articles")
    suspend fun searchArticles(
        @Query("_title_contains")
        searchQuery: String,
        @Query("_start")
        articlesToSkip: Int = 0
    ): Response<ArticlesResponse>
}
