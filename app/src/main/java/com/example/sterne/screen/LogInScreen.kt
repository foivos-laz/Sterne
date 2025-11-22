package com.example.sterne.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapp.LocalAppLanguage
import com.example.sterne.viewmodel.AuthViewModel
import com.example.sterne.R
import com.example.sterne.createLocalizedContext

@Composable
fun LogInScreen(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel = viewModel()) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    var context = LocalContext.current

    val language = LocalAppLanguage.current
    val localizedContext = remember(language) { context.createLocalizedContext(language) }

    Column(modifier = Modifier.fillMaxSize()
        .background(Color(0xFF67282D))) {
        Column(modifier = Modifier.fillMaxSize()
            .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = localizedContext.getString(R.string.loginText1),modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                ),
                color = Color(0xFFF6E9CF)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = localizedContext.getString(R.string.loginText2),modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = 22.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Thin
                ),
                color = Color(0xFFF6E9CF)
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = localizedContext.getString(R.string.loginBox1)) },
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = localizedContext.getString(R.string.loginBox2)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
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

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                isLoading = true
                authViewModel.login(email, password) { success, message ->
                    if (success) {
                        isLoading = false
                        navController.navigate("home"){
                            popUpTo("auth") {inclusive = true}
                        }
                    } else {
                        isLoading = false
                        Toast.makeText(context, message ?: "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF6E9CF)),
                modifier = Modifier.fillMaxWidth()
                    .height(60.dp)
            ){
                Text(text = if (isLoading) localizedContext.getString(R.string.loginButton2) else localizedContext.getString(R.string.loginButton), style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                ),
                    color = Color(0xFF67282D)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = localizedContext.getString(R.string.loginText3), style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Thin,
                    textAlign = TextAlign.Center
                ),
                    color = Color(0xFFF6E9CF)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(text = localizedContext.getString(R.string.loginText4),
                    modifier = Modifier.clickable {
                        navController.navigate("signup")
                    },
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline
                    ),
                    color = Color(0xFFF6E9CF)
                )
            }
        }
    }
}