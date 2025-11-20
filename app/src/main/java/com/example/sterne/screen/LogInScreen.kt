package com.example.sterne.screen

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sterne.viewmodel.AuthViewModel
import com.example.sterne.viewmodel.LoginState

@Composable
fun LogInScreen(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel = viewModel()) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }


    val context = LocalContext.current
    val activity = context as? Activity

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF67282D))) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Welcome back!",modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                ),
                color = Color(0xFFF6E9CF)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Please log in to continue",modifier = Modifier.fillMaxWidth(),
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
                label = { Text(text = "Email") },
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
                label = { Text(text = "Password") },
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
                authViewModel.login(email, password) { loginState ->
                    when (loginState) {
                        is LoginState.Success -> {
                            isLoading = false
                            navController.navigate("home"){
                                popUpTo("auth") {inclusive = true}
                            }
                        }
                        is LoginState.Failure -> {
                            isLoading = false
                            Toast.makeText(context,  "Something went wrong", Toast.LENGTH_SHORT).show()
                        }
                        is LoginState.MultiFactorRequired -> {
                            // Check if the activity is not null before calling the function
                            if (activity != null) {
                                authViewModel.sendSmsVerification(activity) { success, message ->
                                    if (success) {
                                        // Inform user that SMS was sent
                                        Toast.makeText(context, "SMS sent!", Toast.LENGTH_SHORT).show()
                                        // TODO: Navigate to the SMS code entry screen
                                        showDialog = true
                                    } else {
                                        // Show error
                                        Toast.makeText(context, "Error: $message", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } else {
                                // Handle the unlikely case where the activity is null
                                isLoading = false
                                Toast.makeText(context, "Error: Could not find Activity.", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF6E9CF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ){
                Text(text = if (isLoading) "Logging in..." else "Log In", style = TextStyle(
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
                Text(text = "You don't have an account?", style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Thin,
                    textAlign = TextAlign.Center
                ),
                    color = Color(0xFFF6E9CF)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(text = "Create one!",
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

                if (showDialog) {
                    Sms2FADialog(
                        onDismiss = { showDialog = false },
                        onVerify = { code ->
                            showDialog = false
                            // TODO: Call your ViewModel to finish login using this 2FA code
                            // authViewModel.verify2FA(code)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Sms2FADialog(
    onDismiss: () -> Unit,
    onVerify: (String) -> Unit
) {
    var code by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(20.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Enter 6-digit code",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF67282D)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(6) { index ->
                        val char = code.getOrNull(index)?.toString() ?: ""
                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .border(
                                    width = 2.dp,
                                    color = Color(0xFF67282D),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF67282D)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }

                // Invisible Text Field (captures input)
                BasicTextField(
                    value = code,
                    onValueChange = {
                        if (it.length <= 6 && it.all(Char::isDigit)) code = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    modifier = Modifier.size(0.dp) // hidden
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF67282D))
                    ) { Text("Cancel") }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { if (code.length == 6) onVerify(code) },
                        enabled = code.length == 6,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67282D))
                    ) {
                        Text("Verify", color = Color.White)
                    }
                }
            }
        }
    }
}