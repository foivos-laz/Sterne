package com.example.sterne.viewmodel

import PostModel
import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class CommunityViewModel : ViewModel() {

    private val _posts = MutableStateFlow<List<PostModel>>(emptyList())
    val posts = _posts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    @SuppressLint("MissingPermission") // Permissions are checked in the Composable
    fun fetchNearbyPosts(fusedLocationClient: FusedLocationProviderClient) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Get user's current location
                val userLocation: Location? = fusedLocationClient.lastLocation.await()

                if (userLocation == null) {
                    // Handle case where location is not available
                    _posts.value = emptyList()
                    _isLoading.value = false
                    return@launch
                }

                // 2. Calculate the timestamp for 2 days ago for the query
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR, -2)
                val twoDaysAgo = Timestamp(calendar.time)

                // 3. Query Firestore for posts from the last 2 days
                val querySnapshot = FirebaseFirestore.getInstance().collection("posts")
                    .whereGreaterThan("timestamp", twoDaysAgo)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .await()

                val allRecentPosts = querySnapshot.toObjects(PostModel::class.java)

                // 4. Filter the recent posts by distance (within 500 meters)
                val nearbyPosts = allRecentPosts.filter { post ->
                    if (post.location == null) return@filter false

                    val postLocation = Location("").apply {
                        latitude = post.location.latitude
                        longitude = post.location.longitude
                    }

                    val distanceInMeters = userLocation.distanceTo(postLocation)
                    distanceInMeters < 500
                }

                _posts.value = nearbyPosts
            } catch (e: Exception) {
                // Handle exceptions (e.g., log the error)
                _posts.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
