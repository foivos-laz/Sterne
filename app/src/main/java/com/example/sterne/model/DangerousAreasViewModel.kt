import androidx.lifecycle.ViewModel
import com.example.sterne.model.polygonModel
import com.google.firebase.firestore.FirebaseFirestore
import com.mapbox.geojson.Point
import com.mapbox.turf.TurfConstants
import com.mapbox.turf.TurfMeasurement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class DangerousAreasViewModel : ViewModel() {

    private val _polygons = MutableStateFlow<List<polygonModel>>(emptyList())
    val polygons: StateFlow<List<polygonModel>> = _polygons

    suspend fun fetchNearbyPolygons(userLocation: Point, radiusKm: Double = 1.0) {
        try {
            val snapshot = FirebaseFirestore.getInstance().collection("polygons").get().await()
            val nearby = snapshot.documents.mapNotNull { doc ->
                val polygon = doc.toObject(polygonModel::class.java) ?: return@mapNotNull null

                // For distance calculation, convert the stored center GeoPoint to a Mapbox Point.
                val centerMapboxPoint = polygon.center?.let {
                    // --- FIX IS HERE ---
                    // Access properties directly on the Firebase GeoPoint object.
                    Point.fromLngLat(it.longitude, it.latitude)
                }

                // Perform the distance check
                if (centerMapboxPoint != null) {
                    val distanceKm = TurfMeasurement.distance(userLocation, centerMapboxPoint, TurfConstants.UNIT_KILOMETERS)
                    if (distanceKm <= radiusKm) polygon else null
                } else {
                    null
                }
            }
            // Update the StateFlow directly. The UI will react automatically.
            _polygons.value = nearby
        } catch (e: Exception) {
            // Handle exceptions, e.g., log the error
            _polygons.value = emptyList()
        }
    }

    // You might want a temporary data class for this to avoid confusion
    data class polygonModelWithMapbox(
        val points: List<Point>,
        val center: Point
    )

}
