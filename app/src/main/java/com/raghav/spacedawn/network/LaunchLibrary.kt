package com.raghav.spacedawn.network

import com.raghav.spacedawn.models.launchlibrary.LaunchLibraryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LaunchLibrary {

    @GET("upcoming")
    suspend fun getLaunches(
        @Query("offset")
        offset: Int = 0
    ): Response<LaunchLibraryResponse>
}
