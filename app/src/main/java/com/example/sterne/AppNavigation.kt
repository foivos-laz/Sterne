package com.example.sterne

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sterne.screen.AICallScreen
import com.example.sterne.screen.AuthScreen
import com.example.sterne.screen.HomeScreen
import com.example.sterne.screen.LogInScreen
import com.example.sterne.screen.SettingsScreen
import com.example.sterne.screen.SignUpScreen
import com.example.sterne.screen.TutorialScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    val isLoggedIn = Firebase.auth.currentUser!=null
    val  firstPage = if(isLoggedIn) "home" else "auth"


    val LocalAppLanguage = staticCompositionLocalOf { "en" }
    var currentLanguage by remember { mutableStateOf("en") }

    CompositionLocalProvider(LocalAppLanguage provides currentLanguage) {
        NavHost(navController = navController, startDestination = firstPage){
            composable("auth"){
                AuthScreen(modifier, navController)
            }

            composable("signup"){
                SignUpScreen(modifier, navController)
            }

            composable("login"){
                LogInScreen(modifier, navController)
            }

            composable("home"){
                HomeScreen(modifier, navController)
            }

            composable("settings"){
                SettingsScreen(modifier, navController, onToggleLanguage = {
                    currentLanguage = if (currentLanguage == "en") "el" else "en"
                })
            }

            composable("tutorial"){
                TutorialScreen(modifier, navController)
            }

            composable("call") {
                AICallScreen(modifier, navController)
            }
        }
    }
}