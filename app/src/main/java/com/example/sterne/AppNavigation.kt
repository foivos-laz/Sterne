package com.example.sterne

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sterne.screen.AuthScreen
import com.example.sterne.screen.HomeScreen
import com.example.sterne.screen.LogInScreen
import com.example.sterne.screen.SignUpScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "auth"){
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
            HomeScreen(modifier)
        }

    }
}