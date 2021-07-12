package com.raghav.spacedawn.models.launchlibrary

data class LaunchLibraryResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: MutableList<LaunchLibraryResponseItem>
)