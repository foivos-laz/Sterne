package com.example.sterne.model

import com.google.firebase.Timestamp
import com.mapbox.geojson.Point

data class polygonModel(
    val points: List<Point> = emptyList(),  // List of polygon vertices
    val center: Point? = null,             // Centroid of the polygon
    val createdAt: Timestamp? = null       // Server timestamp for creation
)
