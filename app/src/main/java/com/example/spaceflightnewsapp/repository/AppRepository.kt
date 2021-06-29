package com.example.spaceflightnewsapp.repository

import com.example.spaceflightnewsapp.network.SpaceFlightAPI

class AppRepository(
    private val api : SpaceFlightAPI
) {
    suspend fun getArticles() = api.getArticles()
    suspend fun searchArticle(searchQuery : String) = api.searchArticles(searchQuery)
}