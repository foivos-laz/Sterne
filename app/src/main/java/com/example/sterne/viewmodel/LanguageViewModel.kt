package com.example.sterne.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LanguageViewModel : ViewModel() {
    var currentLanguage by mutableStateOf("en")
}