package com.example.sterne

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapp.LanguageDataStore
import com.example.myapp.LocalAppLanguage
import com.example.sterne.screen.AICallScreen
import com.example.sterne.screen.AuthScreen
import com.example.sterne.screen.HomeScreen
import com.example.sterne.screen.LogInScreen
import com.example.sterne.screen.SettingsScreen
import com.example.sterne.screen.SignUpScreen
import com.example.sterne.screen.TutorialScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    val isLoggedIn = Firebase.auth.currentUser!=null
    val  firstPage = if(isLoggedIn) "home" else "auth"
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val currentLanguage by LanguageDataStore.getLanguage(context)
        .collectAsState(initial = "en")

    CompositionLocalProvider(LocalAppLanguage provides currentLanguage) {
        NavHost(navController = navController, startDestination = firstPage){
            composable("auth"){
                AuthScreen(modifier, navController, onToggleLanguage = {
                    val newLang = if (currentLanguage == "en") "el" else "en"

                    // Save persistently
                    scope.launch {
                        LanguageDataStore.saveLanguage(context, newLang)
                    }
                })
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
                    val newLang = if (currentLanguage == "en") "el" else "en"

                    // Save persistently
                    scope.launch {
                        LanguageDataStore.saveLanguage(context, newLang)
                    }
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

fun Context.createLocalizedContext(languageCode: String): Context {
    val config = Configuration(resources.configuration)
    config.setLocale(Locale.forLanguageTag(languageCode))
    return createConfigurationContext(config)
}