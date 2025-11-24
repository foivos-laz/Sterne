package com.example.sterne.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sterne.model.GuideModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun GuideView(modifier: Modifier = Modifier, navController: NavController) {
    val guidesList = remember {
        mutableStateOf<List<GuideModel>>(emptyList())
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        val currentLanguage = context.resources.configuration.locales[0].language
        val collectionName = if (currentLanguage == "el") {
            "guides_gr"
        } else {
            "guides_en"
        }

        Firebase.firestore.collection(collectionName)
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    guidesList.value = it.result.map { document ->
                        val guide = document.toObject(GuideModel::class.java)
                        guide.copy(id = document.id)
                    }
                }
            }
    }

    LazyRow() {
        items(guidesList.value){ item->
            GuideItem(guide = item, navController)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun GuideItem(guide : GuideModel, navController: NavController){
    Card(
        modifier = Modifier.fillMaxWidth()
            .height(100.dp)
            .padding(4.dp)//.size(100.dp)
            .clickable{
                navController.navigate("guidesdetail"+guide.id)
            },
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(1.dp, Color(0xFF67282D)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF6E9CF))
    ){
        Column (modifier = Modifier.fillMaxSize().padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = guide.name, modifier = Modifier,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF67282D)
                )
            )
        }
    }
}