package com.example.sterne.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.example.sterne.R

@Composable
fun AuthScreen(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxSize()
        .background(Color(0xFF1800AD))){
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painter = painterResource(id = R.drawable.just_sterne), contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp))

            //Spacer(modifier = Modifier.height(10.dp))

            Text(text = "Your safety all in one app.", style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
                ),
                color = Color(0xFFF6E9CF)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Please either login or register to continue.", style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Thin,
                textAlign = TextAlign.Center
            ),
                color = Color(0xFFF6E9CF)
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedButton(onClick = { /*TODO*/ },
                border = BorderStroke(1.dp, Color(0xFFF6E9CF)),
                modifier = Modifier.fillMaxWidth()
                    .height(60.dp)
                ){
                Text(text = "Login", style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Thin,
                    textAlign = TextAlign.Center
                ),
                    color = Color(0xFFF6E9CF)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(onClick = { /*TODO*/ },
                border = BorderStroke(1.dp, Color(0xFFF6E9CF)),
                modifier = Modifier.fillMaxWidth()
                    .height(60.dp)
            ){
                Text(text = "Register", style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Thin,
                    textAlign = TextAlign.Center
                ),
                    color = Color(0xFFF6E9CF)
                )
            }

        }
    }
}