package com.example.sterne.screen

import DangerousAreasViewModel
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sterne.model.polygonModel
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
import com.mapbox.maps.extension.style.sources.getSourceAs
import com.mapbox.maps.plugin.locationcomponent.location
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.collections.map

@Composable
fun DangerousAreasScreen(modifier: Modifier = Modifier, navController: NavController, viewModel: DangerousAreasViewModel = viewModel()) {
    val language = LocalAppLanguage.current
    val context = LocalContext.current
    val localizedContext = remember(language) { context.createLocalizedContext(language) }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF6E9CF))){
        Column(
            modifier = Modifier
                .fillMaxSize()
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

            val polygons by viewModel.polygons.collectAsState()

            val coroutineScope = rememberCoroutineScope()

            // 2. Create the stable lambda using remember
            val onUserLocationUpdate = remember(viewModel, coroutineScope) {
                { userPoint: Point ->coroutineScope.launch {
                    viewModel.fetchNearbyPolygons(userPoint)
                }
                    Unit // Explicitly return Unit
                }
            }

            // 4. Pass the stable lambda to your MapScreen
            MapScreen1(
                polygons = polygons,
                onUserLocationUpdate = onUserLocationUpdate
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                navController.navigate("createDangerousArea"){
                    popUpTo("dangerousareas") {inclusive = true}
                }
            },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67282D)),
                modifier = Modifier
                    .fillMaxWidth()
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
                navController.popBackStack()
            },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67282D)),
                modifier = Modifier
                    .fillMaxWidth()
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
fun MapboxAndroidView1(
    modifier: Modifier = Modifier,
    polygons: List<polygonModel>, // Receive state
    onUserLocationUpdate: (Point) -> Unit // Callback to trigger fetch
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    // Remember the listener to avoid re-attaching it on every recomposition.
    val listener = remember { { userPoint: Point -> onUserLocationUpdate(userPoint) } }

    AndroidView(
        modifier = modifier,
        factory = { mapView },
        update = { mapViewInstance ->
            val mapboxMap = mapViewInstance.mapboxMap
            mapboxMap.loadStyle(
                Style.STANDARD
            ) { style ->
                val locationPlugin = mapViewInstance.location
                locationPlugin.updateSettings { enabled = true; pulsingEnabled = true }

                if (checkLocationPermission(context)) { // This will now work correctly
                    locationPlugin.addOnIndicatorPositionChangedListener(listener)
                    locationPlugin.addOnIndicatorPositionChangedListener { point ->
                        mapboxMap.setCamera(CameraOptions.Builder().center(point).zoom(15.0).build())
                    }
                }
                updateMapWithPolygons(mapViewInstance, polygons) // This will also work
            }
        }
    )
} // End of MapboxAndroidView1

@Composable
fun MapScreen1(
    polygons: List<polygonModel>,
    onUserLocationUpdate: (Point) -> Unit
) {
    Box(
        modifier = Modifier
            .size(300.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, Color.Gray, RoundedCornerShape(20.dp))
    ) {
        MapboxAndroidView1(modifier = Modifier.fillMaxSize(), polygons = polygons, onUserLocationUpdate = onUserLocationUpdate)
    }
}

// --- FIX: MOVE HELPER FUNCTIONS OUTSIDE ---

// Move updateMapWithPolygons to be a top-level function in the file
private fun updateMapWithPolygons(mapView: MapView, polygons: List<polygonModel>) {
    val features = polygons.map { model ->
        val mapboxPoints = model.points.map { geoPoint ->
            Point.fromLngLat(geoPoint.longitude, geoPoint.latitude)
        }
        val polygonGeometry = Polygon.fromLngLats(listOf(mapboxPoints))
        Feature.fromGeometry(polygonGeometry)
    }
    val featureCollection = FeatureCollection.fromFeatures(features)

    mapView.mapboxMap.getStyle { style ->
        val source = style.getSourceAs<GeoJsonSource>("polygon-source")
        if (source == null) {
            style.addSource(
                GeoJsonSource.Builder("polygon-source")
                    .featureCollection(featureCollection)
                    .build()
            )
            style.addLayer(
                FillLayer("polygon-layer", "polygon-source").apply {
                    fillColor(rgba(255.0, 0.0, 0.0, 0.33))
                    fillOutlineColor(rgba(255.0, 0.0, 0.0, 1.0))
                }
            )
        } else {
            source.featureCollection(featureCollection)
        }
    }
}

// Move checkLocationPermission to be a top-level function in the file
private fun checkLocationPermission(context: android.content.Context): Boolean {
    return ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}