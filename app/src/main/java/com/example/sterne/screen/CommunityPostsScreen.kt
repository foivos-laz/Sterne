package com.example.sterne.screen

import PostModel
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
// --- ADD THESE IMPORTS ---
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
// ---
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.LocalAppLanguage
import com.example.sterne.R
import com.example.sterne.createLocalizedContext
import com.example.sterne.viewmodel.CommunityViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CommunityPostsScreen(modifier: Modifier = Modifier,  viewModel: CommunityViewModel = viewModel()) {
    val language = LocalAppLanguage.current
    val context = LocalContext.current
    val localizedContext = remember(language) { context.createLocalizedContext(language) }
    val coroutineScope = rememberCoroutineScope()

    // --- State from ViewModel ---
    val posts by viewModel.posts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }

    val fusedLocationClient = remember {
        com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(context)
    }

    // --- Data Fetching Logic ---
    LaunchedEffect(key1 = true) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModel.fetchNearbyPosts(fusedLocationClient)
        }
    }


    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                isUploading = true
                coroutineScope.launch {
                    uploadPost(
                        fusedLocationClient = fusedLocationClient,
                        content = inputText,
                        onSuccess = {
                            showDialog = false
                            inputText = ""
                            isUploading = false
                            Toast.makeText(context, "Post uploaded successfully!", Toast.LENGTH_SHORT).show()
                            viewModel.fetchNearbyPosts(fusedLocationClient)
                        },
                        onFailure = { e ->
                            isUploading = false
                            Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    )
                }
            } else {
                Toast.makeText(context, "Location permission is required to create a post.", Toast.LENGTH_LONG).show()
            }
        }
    )

    val handleUploadClick = {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) -> {
                isUploading = true
                coroutineScope.launch {
                    uploadPost(
                        fusedLocationClient = fusedLocationClient,
                        content = inputText,
                        onSuccess = {
                            showDialog = false
                            inputText = ""
                            isUploading = false
                            Toast.makeText(context, "Post uploaded successfully!", Toast.LENGTH_SHORT).show()
                            viewModel.fetchNearbyPosts(fusedLocationClient)
                        },
                        onFailure = { e ->
                            isUploading = false
                            Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    )
                }
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6E9CF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = localizedContext.getString(R.string.communityPostsScrText1), style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                ),
                color = Color(0xFF67282D)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.weight(1f)) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color(0xFF67282D))
                } else if (posts.isEmpty()) {
                    Text(
                        text = "No posts found nearby.\nBe the first to create one!",
                        color = Color(0xFF67282D),
                        textAlign = TextAlign.Center
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(posts) { post ->
                            PostItem(post = post)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = localizedContext.getString(R.string.communityPostsScrText2), style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Thin,
                    textAlign = TextAlign.Center
                ),
                color = Color(0xFF67282D)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67282D)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text(
                    text = localizedContext.getString(R.string.communtyPostsScrButton1), style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    ),
                    color = Color(0xFFF6E9CF)
                )
            }
        }
    }


    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF67282D),
                tonalElevation = 8.dp,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = localizedContext.getString(R.string.communtyPostsScrButton1), color = Color(0xFFF6E9CF), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = inputText,
                        onValueChange = { inputText = it},
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = localizedContext.getString(R.string.communityPostsScrText3))
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color(0xFFF6E9CF),
                            unfocusedIndicatorColor = Color(0xFFF6E9CF),
                            cursorColor = Color(0xFFF6E9CF),
                            focusedLabelColor = Color(0xFFF6E9CF),
                            unfocusedLabelColor = Color(0xFFF6E9CF),
                            focusedContainerColor = Color(0xFF67282D),
                            unfocusedContainerColor = Color(0xFF67282D),
                            focusedTextColor = Color(0xFFF6E9CF),
                            unfocusedTextColor = Color(0xFFF6E9CF)
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text= localizedContext.getString(R.string.communityPostsScrText4)
                        ,color = Color(0xFFF6E9CF), fontSize = 12.sp, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center){
                        Button(
                            onClick = {
                                showDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF6E9CF)
                            )
                        ) {
                            Text(text = localizedContext.getString(R.string.communityPostsScrButton2), color = Color(0xFF67282D))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (inputText.isNotBlank() && !isUploading) {
                                    handleUploadClick()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF6E9CF)
                            ),
                            enabled = inputText.isNotBlank() && !isUploading
                        ) {
                            if (isUploading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color(0xFF67282D),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(text = localizedContext.getString(R.string.communityPostsScrButton3), color = Color(0xFF67282D))
                            }
                        }
                    }
                }
            }
        }
    }
}

private suspend fun uploadPost(
    fusedLocationClient: com.google.android.gms.location.FusedLocationProviderClient,
    content: String,
    onSuccess: () -> Unit,    onFailure: (Exception) -> Unit
) {
    try {
        @Suppress("MissingPermission")
        val location = fusedLocationClient.lastLocation.await()
        val geoPoint = if (location != null) {
            GeoPoint(location.latitude, location.longitude)
        } else {
            null
        }
        val post = hashMapOf(
            "content" to content,
            "location" to geoPoint,
            "timestamp" to com.google.firebase.Timestamp.now()
        )
        FirebaseFirestore.getInstance().collection("posts")
            .add(post)
            .await()
        onSuccess()
    } catch (e: Exception) {
        onFailure(e)
    }
}

// --- ADD THIS COMPOSABLE ---
@Composable
fun PostItem(post: PostModel) {
    val formatter = remember { SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()) }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF67282D),
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = post.content,
                color = Color(0xFFF6E9CF),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.timestamp?.toDate()?.let { formatter.format(it) } ?: "Just now",
                color = Color(0xFFF6E9CF).copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        }
    }
}
