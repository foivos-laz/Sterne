import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ServerTimestamp

data class PostModel(
    val content: String = "",
    val location: GeoPoint? = null,
    // Use @ServerTimestamp to let Firestore handle the timestamp on creation
    @ServerTimestamp val timestamp: Timestamp? = null
)
