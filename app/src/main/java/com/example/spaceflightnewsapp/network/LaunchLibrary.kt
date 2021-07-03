package com.example.spaceflightnewsapp.network

import com.example.spaceflightnewsapp.models.launchlibrary.LaunchLibraryResponse
import com.example.spaceflightnewsapp.models.spaceflightapi.ArticlesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LaunchLibrary {

   @GET("upcoming")
   suspend fun getLaunches(
       @Query("offset")
       offset : Int =0
   ):Response<LaunchLibraryResponse>



}