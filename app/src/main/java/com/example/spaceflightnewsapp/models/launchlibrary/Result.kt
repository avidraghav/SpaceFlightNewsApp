package com.example.spaceflightnewsapp.models.launchlibrary

data class Result(
    val failreason: String,
    val hashtag: Any,
    val holdreason: String,
    val id: String,
    val image: String,
    val infographic: Any,
    val last_updated: String,
    val launch_service_provider: LaunchServiceProvider,
    val mission: Mission,
    val name: String,
    val net: String,
    val pad: Pad,
    val probability: Int,
    val program: List<Program>,
    val rocket: Rocket,
    val slug: String,
    val status: Status,
    val url: String,
    val webcast_live: Boolean,
    val window_end: String,
    val window_start: String
)