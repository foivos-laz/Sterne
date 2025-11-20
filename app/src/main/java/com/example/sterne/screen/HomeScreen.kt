package com.example.sterne.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sterne.viewmodel.AuthViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun HomeScreen(modifier: Modifier = Modifier, authViewModel: AuthViewModel = viewModel()) {

    var context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()
        .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Home screen")

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            Firebase.auth.signOut()
        }) {
            Text(text = "Log out")
        }

        Button(onClick = {
            authViewModel.verifyEmailAddress { success, message ->
                if (success) {
                    Toast.makeText(context, message ?: "Email sent successfully", Toast.LENGTH_SHORT).show()
                    // Email sent successfully
                } else {
                    Toast.makeText(context, message ?: "Error occurred while sending email", Toast.LENGTH_SHORT).show()
                    // Error occurred while sending email
                }}
        }) {
            Text(text = "Verify Email")
        }
    }
}