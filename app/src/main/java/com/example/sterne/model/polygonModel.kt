package com.example.sterne.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint // <-- Make sure this import is here

data class polygonModel(
    val points: List<GeoPoint> = emptyList(),
    val center: GeoPoint? = null,
    val createdAt: Timestamp? = null
)


