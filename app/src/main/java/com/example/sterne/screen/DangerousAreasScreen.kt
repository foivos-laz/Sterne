package com.example.sterne.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import androidx.navigation.NavController
import com.example.myapp.LocalAppLanguage
import com.example.sterne.R
import com.example.sterne.createLocalizedContext
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import com.example.sterne.model.polygonModel
import com.google.firebase.firestore.FirebaseFirestore
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.extension.style.expressions.dsl.generated.rgba
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.FillLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.getSource
import com.mapbox.maps.extension.style.sources.getSourceAs
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.turf.TurfMeasurement
import com.mapbox.turf.TurfConstants
import kotlin.collections.map

@Composable
fun DangerousAreasScreen(modifier: Modifier = Modifier, navController: NavController) {
    val language = LocalAppLanguage.current
    val context = LocalContext.current
    val localizedContext = remember(language) { context.createLocalizedContext(language) }

    Column(modifier = Modifier.fillMaxSize()
        .background(Color(0xFFF6E9CF))){
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = localizedContext.getString(R.string.dangerousScrText1), style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            ),
                color = Color(0xFF67282D)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = localizedContext.getString(R.string.dangerousScrText2), style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Thin,
                textAlign = TextAlign.Center
            ),
                color = Color(0xFF67282D)
            )

            Spacer(modifier = Modifier.height(20.dp))

            MapScreen()

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                navController.navigate("home"){
                    popUpTo("tutorial") {inclusive = true}
                }
            },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67282D)),
                modifier = Modifier.fillMaxWidth()
                    .height(40.dp)
            ){
                Text(text = localizedContext.getString(R.string.dangerousScrButton1), style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                ),
                    color = Color(0xFFF6E9CF)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = localizedContext.getString(R.string.settingsText4), style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Thin,
                textAlign = TextAlign.Center
            ),
                color = Color(0xFF67282D)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                navController.navigate("home"){
                    popUpTo("tutorial") {inclusive = true}
                }
            },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67282D)),
                modifier = Modifier.fillMaxWidth()
                    .height(40.dp)
            ){
                Text(text = localizedContext.getString(R.string.settingsButton3), style = TextStyle(
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
}

@Composable
fun MapboxAndroidView(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    AndroidView(
        modifier = modifier,
        factory = { mapView },
        update = { mapViewInstance ->

            val mapboxMap = mapViewInstance.mapboxMap

            mapboxMap.loadStyle(Style.STANDARD) { style ->

                // Enable user location
                val locationPlugin = mapViewInstance.location
                locationPlugin.updateSettings {
                    enabled = true
                    pulsingEnabled = true
                }

                // Center camera on user & fetch nearby polygons
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPlugin.addOnIndicatorPositionChangedListener { userPoint ->

                        // Center camera on user
                        mapboxMap.setCamera(
                            CameraOptions.Builder()
                                .center(userPoint)
                                .zoom(15.0)
                                .build()
                        )

                        // --- FETCH POLYGONS WITHIN 1 KM ---
                        fetchNearbyPolygons(userPoint) { nearbyPolygons ->

                            val features = nearbyPolygons.map { polygon ->
                                Feature.fromGeometry(Polygon.fromLngLats(listOf(polygon.points)))
                            }

                            val featureCollection = FeatureCollection.fromFeatures(features)

                            // Add or update GeoJson source and FillLayer
                            if (style.getSource("polygon-source") == null) {
                                val geoJsonSource = GeoJsonSource.Builder("polygon-source")
                                    .featureCollection(featureCollection)
                                    .build()
                                style.addSource(geoJsonSource)

                                val fillLayer = FillLayer("polygon-layer", "polygon-source").apply {
                                    fillColor(rgba(255.0, 0.0, 0.0, 0.33))       // semi-transparent red
                                    fillOutlineColor(rgba(255.0, 0.0, 0.0, 1.0)) // solid red outline
                                }
                                style.addLayer(fillLayer)
                            } else {
                                style.getSourceAs<GeoJsonSource>("polygon-source")
                                    ?.featureCollection(featureCollection)
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun MapScreen() {
    Box(
        modifier = Modifier
            .size(300.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, Color.Gray, RoundedCornerShape(20.dp))
    ) {
        MapboxAndroidView(modifier = Modifier.fillMaxSize())  // no polygonDataList needed
    }
}

fun fetchNearbyPolygons(userLocation: Point, radiusKm: Double = 1.0, callback: (List<polygonModel>) -> Unit) {
    FirebaseFirestore.getInstance().collection("polygons")
        .get()
        .addOnSuccessListener { snapshot ->
            val nearby = snapshot.documents.mapNotNull { doc ->
                val points = (doc["points"] as? List<Map<String, Any>>)?.map { p ->
                    Point.fromLngLat(p["lng"] as Double, p["lat"] as Double)
                } ?: return@mapNotNull null

                val centerMap = doc["center"] as? Map<String, Any>
                val centerPoint = centerMap?.let { Point.fromLngLat(it["lng"] as Double, it["lat"] as Double) }

                // Filter by distance
                if (centerPoint != null) {
                    val distanceKm = TurfMeasurement.distance(userLocation, centerPoint, "kilometers")
                    if (distanceKm <= radiusKm) polygonModel(points, centerPoint) else null
                } else null
            }
            callback(nearby)
        }
}