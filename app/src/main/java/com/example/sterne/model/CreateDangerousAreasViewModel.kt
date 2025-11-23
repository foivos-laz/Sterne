package com.example.sterne.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sterne.model.polygonModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.turf.TurfMeasurement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CreateDangerousAreasViewModel : ViewModel() {

    private val _drawnPoints = MutableStateFlow<List<Point>>(emptyList())
    val drawnPoints: StateFlow<List<Point>> = _drawnPoints.asStateFlow()

    fun addPoint(point: Point) {
        _drawnPoints.value = _drawnPoints.value + point
    }

    fun clearPoints() {
        _drawnPoints.value = emptyList()
    }

    fun undoLastPoint() {
        if (_drawnPoints.value.isNotEmpty()) {
            _drawnPoints.value = _drawnPoints.value.dropLast(1)
        }
    }

    fun savePolygon(pointsToSave: List<Point>, callback: (Boolean) -> Unit) {
        if (pointsToSave.size < 3) {
            callback(false)
            return
        }

        viewModelScope.launch {
            try {
                Log.d("SavePolygon", "Attempting to save with ${pointsToSave.size} points.")

                val polygonGeometry = Polygon.fromLngLats(listOf(pointsToSave + pointsToSave.first()))
                val polygonFeature = Feature.fromGeometry(polygonGeometry)
                val centerPointFeature = TurfMeasurement.center(polygonFeature)
                val centerMapboxPoint = centerPointFeature.geometry() as Point?

                Log.d("SavePolygon", "Centroid calculation successful.")

                val firestorePoints = pointsToSave.map { mapboxPoint ->
                    GeoPoint(mapboxPoint.latitude(), mapboxPoint.longitude())
                }

                val firestoreCenter = centerMapboxPoint?.let {
                    GeoPoint(it.latitude(), it.longitude())
                }

                // --- FIX IS HERE ---
                // Remove the unnecessary cast. 'firestoreCenter' is already the correct type (GeoPoint?).
                val newPolygon = polygonModel(
                    points = firestorePoints,
                    center = firestoreCenter,
                    createdAt = Timestamp.now()
                )
                // --- END OF FIX ---

                FirebaseFirestore.getInstance().collection("polygons").add(newPolygon).await()

                Log.d("SavePolygon", "Firestore upload successful.")

                clearPoints()
                callback(true)
            } catch (e: Exception) {
                Log.e("SavePolygonError", "Failed to save polygon", e)
                callback(false)
            }
        }
    }
}
