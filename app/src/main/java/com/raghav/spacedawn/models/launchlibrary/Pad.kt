package com.raghav.spacedawn.models.launchlibrary

data class Pad(
    val agency_id: Any,
    val id: Int,
    val info_url: Any,
    val latitude: String,
    val location: Location,
    val longitude: String,
    val map_image: String,
    val map_url: String,
    val name: String,
    val total_launch_count: Int,
    val url: String,
    val wiki_url: String
)
