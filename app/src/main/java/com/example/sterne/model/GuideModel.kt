package com.example.sterne.model

import com.google.firebase.firestore.PropertyName

data class GuideModel(
    var id: String = "",
    val name: String = "",
    val description: String = "",
    @get:PropertyName("needInternet")
    val needsInternet: Boolean = false,
    val specialPermissions: List<String> = emptyList(),
    @get:PropertyName("functionality ")
    val functionality: Boolean = false
)
