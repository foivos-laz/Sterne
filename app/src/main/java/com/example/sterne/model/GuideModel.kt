package com.example.sterne.model

data class GuideModel(
    var id: String = "",
    val name: String = "",
    val description: String = "",
    val needsInternet: Boolean = false,
    val specialPermissions: List<String> = emptyList(),
    val functionality: Boolean = false
)
