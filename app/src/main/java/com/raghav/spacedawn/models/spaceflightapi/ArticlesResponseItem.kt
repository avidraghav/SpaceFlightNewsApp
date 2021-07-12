package com.raghav.spacedawn.models.spaceflightapi

import java.io.Serializable

data class ArticlesResponseItem(
    val events: List<Any>,
    val featured: Boolean,
    val id: Int,
    val imageUrl: String,
    val launches: List<Launche>,
    val newsSite: String,
    val publishedAt: String,
    val summary: String,
    val title: String,
    val updatedAt: String,
    val url: String
): Serializable