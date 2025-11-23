package com.example.sterne.screen

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
import androidx.navigation.NavController
import com.example.myapp.LocalAppLanguage
import com.example.sterne.createLocalizedContext

@Composable
fun NeartestOpenStoreScreen(modifier: Modifier = Modifier, navController: NavController) {
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
            Text(text = "Find the nearest open store", style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            ),
                color = Color(0xFF67282D)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "By pressing the 'Find Store' button, the app will search the nearest open store and show its name and location on the map", style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Thin,
                textAlign = TextAlign.Center
            ),
                color = Color(0xFF67282D)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                navController.navigate("found")
            },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67282D)),
                modifier = Modifier.fillMaxWidth()
                    .height(40.dp)
            ){
                Text(text = "Find Store", style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                ),
                    color = Color(0xFFF6E9CF)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Click the button below to go back home", style = TextStyle(
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
                modifier = Modifier.fillMaxWidth()
                    .height(40.dp)
            ){
                Text(text = "Return to Home", style = TextStyle(
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