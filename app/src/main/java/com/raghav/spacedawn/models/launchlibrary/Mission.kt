package com.raghav.spacedawn.models.launchlibrary

data class Mission(
    val description: String,
    val id: Int,
    val launch_designator: Any,
    val name: String,
    val orbit: Orbit,
    val type: String
)