package com.example.sterne.screen

import android.widget.Toast
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sterne.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.example.sterne.viewmodel.AuthViewModel

@Composable
fun SettingsScreen(modifier: Modifier = Modifier, navController: NavController) {

    val user = Firebase.auth.currentUser
    val emailAddressChecker = user?.isEmailVerified
    var context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()
        .background(Color(0xFFF6E9CF))){
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Settings", style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            ),
                color = Color(0xFF67282D)
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (emailAddressChecker == false){
                Text(text = "To verify your email address, click the button below", style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Thin,
                    textAlign = TextAlign.Center
                ),
                    color = Color(0xFF67282D)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(onClick = {
                    val authViewModel = AuthViewModel()
                    authViewModel.verifyEmailAddress { success, message ->
                        if (success) {
                            Toast.makeText(context, message ?: "Email sent successfully", Toast.LENGTH_SHORT).show()
                            // Email sent successfully
                        } else {
                            Toast.makeText(context, message ?: "Error occurred while sending email", Toast.LENGTH_SHORT).show()
                            // Error occurred while sending email
                        }}
                },
                    border = BorderStroke(1.dp, Color(0xFF67282D)),
                    modifier = Modifier.fillMaxWidth()
                        .height(40.dp)
                ){
                    Text(text = "Verify Email Address", style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    ),
                        color = Color(0xFF67282D)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Click the button below to log out", style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Thin,
                textAlign = TextAlign.Center
            ),
                color = Color(0xFF67282D)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                Firebase.auth.signOut()

                navController.navigate("auth"){
                    popUpTo("home") {inclusive = true}
                }
            },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67282D)),
                modifier = Modifier.fillMaxWidth()
                    .height(40.dp)
            ){
                Text(text = "Sign Out", style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                ),
                    color = Color(0xFFF6E9CF)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Click the button below to return back to the home screen", style = TextStyle(
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