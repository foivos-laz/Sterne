package com.example.sterne.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sterne.R
import com.example.sterne.viewmodel.CreateDangerousAreasViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.style.expressions.dsl.generated.rgba
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.FillLayer
import com.mapbox.maps.extension.style.layers.generated.LineLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.getSourceAs
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.locationcomponent.location

@Composable
fun CreateDangerousAreasScreen(
    navController: NavController,
    viewModel: CreateDangerousAreasViewModel = viewModel()
) {
    val context = LocalContext.current
    val drawnPoints by viewModel.drawnPoints.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6E9CF))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Mark a New Area",
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            ),
            color = Color(0xFF67282D)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tap on the map to draw a polygon. You need at least 3 points.",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Thin,
                textAlign = TextAlign.Center
            ),
            color = Color(0xFF67282D)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Map View for drawing
        DrawingMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            points = drawnPoints,
            onMapClick = { viewModel.addPoint(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.undoLastPoint() },
                enabled = drawnPoints.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67282D)),
                modifier = Modifier.weight(1f)
            ) {
                Text("Undo", color = Color(0xFFF6E9CF))
            }
            Button(
                onClick = { viewModel.clearPoints() },
                enabled = drawnPoints.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67282D)),
                modifier = Modifier.weight(1f)
            ) {
                Text("Clear", color = Color(0xFFF6E9CF))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Save Button
        Button(
            onClick = {
                // Pass the current state of drawnPoints directly to the function.
                viewModel.savePolygon(drawnPoints) { success ->
                    if (success) {
                        Toast.makeText(context, "Area saved successfully!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Failed to save. A polygon needs at least 3 points.", Toast.LENGTH_LONG).show()
                    }
                }
            },
            enabled = drawnPoints.size >= 3,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9E473F)),
            modifier = Modifier.fillMaxWidth().height(40.dp)
        ) {
            Text(
                text = "Save Area",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color(0xFFF6E9CF)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Back Button
        Button(onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67282D)),
            modifier = Modifier.fillMaxWidth().height(40.dp)
        ){
            Text(text = "Back", style = TextStyle(
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

@Composable
private fun DrawingMap(
    modifier: Modifier = Modifier,
    points: List<Point>,
    onMapClick: (Point) -> Unit
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, Color.Gray, RoundedCornerShape(20.dp))
    ) {
        AndroidView(
            factory = {
                mapView.apply {
                    mapboxMap.loadStyle(Style.STANDARD)

                    // Center on user's location if available
                    val locationPlugin = this.location
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationPlugin.enabled = true
                        locationPlugin.addOnIndicatorPositionChangedListener { userPoint ->
                            mapboxMap.setCamera(cameraOptions { center(userPoint); zoom(14.0) })
                            // Make sure listener is removed after first location update to prevent map from re-centering
                            locationPlugin.removeOnIndicatorPositionChangedListener {  }
                        }
                    }

                    // Add map click listener
                    mapboxMap.addOnMapClickListener { point ->
                        onMapClick(point)
                        true
                    }
                }
            },
            update = {
                // This block is called when 'points' state changes
                mapView.mapboxMap.getStyle { style ->
                    val sourceId = "polygon-drawing-source"
                    val fillLayerId = "polygon-drawing-fill"
                    val lineLayerId = "polygon-drawing-line"

                    val source = style.getSourceAs<GeoJsonSource>(sourceId)

                    // Create a valid polygon for display (must be closed)
                    val polygonPoints = if (points.size > 2) {
                        listOf(points + points.first())
                    } else {
                        listOf(points)
                    }

                    if (source == null) {
                        style.addSource(GeoJsonSource.Builder(sourceId).geometry(com.mapbox.geojson.Polygon.fromLngLats(polygonPoints)).build())
                        // Add fill layer for the polygon area
                        style.addLayer(
                            FillLayer(fillLayerId, sourceId).apply {
                                fillColor(rgba(255.0, 0.0, 0.0, 0.3))
                            }
                        )
                        // Add line layer for the polygon outline
                        style.addLayer(
                            LineLayer(lineLayerId, sourceId).apply {
                                lineColor("red")
                                lineWidth(2.0)
                            }
                        )
                    } else {
                        // Update existing source with new points
                        source.geometry(com.mapbox.geojson.Polygon.fromLngLats(polygonPoints))
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
