package com.example.myapp

import androidx.compose.runtime.staticCompositionLocalOf

// Holds the current language code ("en", "el", etc.)
val LocalAppLanguage = staticCompositionLocalOf { "en" }