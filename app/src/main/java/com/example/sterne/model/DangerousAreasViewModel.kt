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

    // Use suspend function for asynchronous work
    suspend fun fetchNearbyPolygons(userLocation: Point, radiusKm: Double = 1.0) {
        val db = FirebaseFirestore.getInstance()
        try {
            val snapshot = db.collection("polygons").get().await() // Use await for cleaner async code
            val nearby = snapshot.documents.mapNotNull { doc ->
                val pointsList = (doc["points"] as? List<Map<String, Any>>)?.map { p ->
                    Point.fromLngLat(p["lng"] as Double, p["lat"] as Double)
                } ?: return@mapNotNull null

                val centerMap = doc["center"] as? Map<String, Any>
                val centerPoint = centerMap?.let { Point.fromLngLat(it["lng"] as Double, it["lat"] as Double) }

                if (centerPoint != null) {
                    val distanceKm = TurfMeasurement.distance(userLocation, centerPoint, TurfConstants.UNIT_KILOMETERS)
                    if (distanceKm <= radiusKm) polygonModel(pointsList, centerPoint) else null
                } else null
            }
            _polygons.value = nearby
        } catch (e: Exception) {
            // Handle exceptions, e.g., log the error
            _polygons.value = emptyList()
        }
    }
}
