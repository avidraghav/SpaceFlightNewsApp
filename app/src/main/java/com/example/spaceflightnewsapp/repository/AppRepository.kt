package com.example.spaceflightnewsapp.repository

import com.example.spaceflightnewsapp.network.LaunchLibrary
import com.example.spaceflightnewsapp.network.SpaceFlightAPI

class AppRepository(
    private val api_spaceflight : SpaceFlightAPI,
    private val api_launchlibrary : LaunchLibrary

) {
    suspend fun getArticles(skipArticles : Int) = api_spaceflight.getArticles(skipArticles)
    suspend fun searchArticle(searchQuery : String,skipArticles: Int) = api_spaceflight.searchArticles(searchQuery,skipArticles)
    suspend fun getLaunches(skipLaunches: Int) = api_launchlibrary.getLaunches(skipLaunches)
}