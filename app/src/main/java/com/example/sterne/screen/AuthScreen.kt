package com.example.sterne.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sterne.R

@Composable
fun AuthScreen(modifier: Modifier = Modifier, navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()
        .background(Color(0xFF67282D))){
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painter = painterResource(id = R.drawable.hera_logo_transparent_beige_preview), contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp))

            //Spacer(modifier = Modifier.height(10.dp))

            Text(text = "Your safety all in one app", style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
                ),
                color = Color(0xFFF6E9CF)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Please either login or register to continue", style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Thin,
                textAlign = TextAlign.Center
            ),
                color = Color(0xFFF6E9CF)
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedButton(onClick = {
                navController.navigate("login")
            },
                border = BorderStroke(1.dp, Color(0xFFF6E9CF)),
                modifier = Modifier.fillMaxWidth()
                    .height(60.dp)
                ){
                Text(text = "Login", style = TextStyle(
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
                navController.navigate("signup")
            },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF6E9CF)),
                    modifier = Modifier.fillMaxWidth()
                    .height(60.dp)
            ){
                Text(text = "Register", style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                ),
                    color = Color(0xFF67282D)
                )
            }
        }
    }
}