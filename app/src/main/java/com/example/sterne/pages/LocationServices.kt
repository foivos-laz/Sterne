package com.example.sterne.pages

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.myapp.LocalAppLanguage
import com.example.sterne.R
import com.example.sterne.createLocalizedContext

@Composable
fun LocationServices(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current

    val language = LocalAppLanguage.current
    val localizedContext = remember(language) { context.createLocalizedContext(language) }

    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        // Check the result of the request
        val fineLocationGranted = permissionsMap[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseLocationGranted = permissionsMap[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (fineLocationGranted || coarseLocationGranted) {
            Toast.makeText(context, "Location permission granted!", Toast.LENGTH_SHORT).show()
            // You can now proceed to get the location if needed
        } else {
            Toast.makeText(context, "Location permission denied.", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        val allPermissionsGranted = permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        if (!allPermissionsGranted) {
            permissionLauncher.launch(permissions)
        } else {
            // Permissions are already granted, proceed with location logic
            // Toast.makeText(context, "Location permission already granted.", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkAndRequestPermissions(context: Context, launcher: androidx.activity.result.ActivityResultLauncher<Array<String>>) : Boolean{
        val allPermissionsGranted = permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        if (allPermissionsGranted) {
            // Permissions are already granted, proceed directly
            return true
        } else {
            // Permissions are not granted, launch the request dialog
            launcher.launch(permissions)
            return false
        }
    }

    Column(modifier = Modifier.fillMaxSize()
        .background(Color(0xFFF6E9CF))){
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = localizedContext.getString(R.string.locationServices), style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            ),
                color = Color(0xFF67282D)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = localizedContext.getString(R.string.locationPgText2), style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Thin,
                textAlign = TextAlign.Center
            ),
                color = Color(0xFF67282D)
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(text = localizedContext.getString(R.string.AICallPGText3), style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Thin,
                textAlign = TextAlign.Center
            ),
                color = Color(0xFF67282D)
            )

            Spacer(modifier = Modifier.height(30.dp))

            val isInternetAvailable = isInternetAvailable(context)

            Button(onClick = {
                if (isInternetAvailable) {
                    navController.navigate("community")
                }
                else {
                    Toast.makeText(context,   "You don't have an internet connection!", Toast.LENGTH_SHORT).show()
                }

            },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67282D)),
                modifier = Modifier.fillMaxWidth()
                    .height(60.dp)
            ){
                Text(text = localizedContext.getString(R.string.locationPgButton1), style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                ),
                    color = Color(0xFFF6E9CF)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                if (isInternetAvailable && checkAndRequestPermissions(context, permissionLauncher)) {
                    navController.navigate("dangerousareas")
                }
                else {
                    Toast.makeText(context,   "You don't have an internet connection!", Toast.LENGTH_SHORT).show()
                }

            },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67282D)),
                modifier = Modifier.fillMaxWidth()
                    .height(60.dp)
            ){
                Text(text = localizedContext.getString(R.string.locationPgButton2), style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                ),
                    color = Color(0xFFF6E9CF)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                if (isInternetAvailable) {
                    navController.navigate("nearest")
                }
                else {
                    Toast.makeText(context,   "You don't have an internet connection!", Toast.LENGTH_SHORT).show()
                }

            },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67282D)),
                modifier = Modifier.fillMaxWidth()
                    .height(60.dp)
            ){
                Text(text = localizedContext.getString(R.string.locationPgButton3), style = TextStyle(
                    fontSize = 20.sp,
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