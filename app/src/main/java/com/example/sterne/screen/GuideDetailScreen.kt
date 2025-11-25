package com.example.sterne.screen

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapp.LocalAppLanguage
import com.example.sterne.R
import com.example.sterne.createLocalizedContext
import com.example.sterne.model.GuideModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun GuideDetailsScreen(modifier: Modifier = Modifier, guideID : String) {
    var guide by remember{
        mutableStateOf(GuideModel())
    }

    val context = LocalContext.current
    val language = LocalAppLanguage.current
    val localizedContext = remember(language) { context.createLocalizedContext(language) }

    LaunchedEffect(key1 = Unit) {
        val collectionName = if (language == "el") {
            "guides_gr"
        } else {
            "guides_en"
        }

        Firebase.firestore.collection(collectionName).document(guideID).get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    val result = it.result.toObject(GuideModel::class.java)
                    if (result != null) {
                        result.id = it.result.id
                        guide = result
                    }
                }
            }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF6E9CF))) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            shape = RoundedCornerShape(5.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            border = BorderStroke(1.dp, Color(0xFF67282D)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF6E9CF))
        ) {
            val scrollState = rememberScrollState()

            //Event Name Area, doesn't scroll with the rest
            Column(
                modifier = Modifier.fillMaxSize().padding(3.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(3.dp)
                        .background(Color(0xFFF6E9CF), shape = RoundedCornerShape(5.dp)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = guide.name, modifier = Modifier,
                        style = TextStyle(
                            fontSize = 25.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF67282D)
                        )
                    )
                }

                //For the rest of the events content
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .fillMaxSize()
                        .background(Color(0xFFF6E9CF)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //The are of the rest of the information
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        //Description
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .background(Color(0xFFF6E9CF), shape = RoundedCornerShape(10.dp)),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = localizedContext.getString(R.string.guideDetailText1), modifier = Modifier
                                    .background(Color(0xFF67282D), shape = RoundedCornerShape(5.dp))
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFF6E9CF)
                                )
                            )

                            //Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = guide.description, modifier = Modifier.padding(10.dp),
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color(0xFF67282D)
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        //Needs Internet Information
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .background(Color(0xFFF6E9CF), shape = RoundedCornerShape(10.dp)),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = localizedContext.getString(R.string.guideDetailText2), modifier = Modifier,
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        color = Color.DarkGray
                                    )
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                if (guide.needsInternet) {
                                    Text(
                                        text = localizedContext.getString(R.string.guideDetailsText7), modifier = Modifier,
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF67282D)
                                        )
                                    )
                                } else {
                                    Text(
                                        text = localizedContext.getString(R.string.guideDetailsText8), modifier = Modifier,
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF67282D)
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        //Permissions Information
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = localizedContext.getString(R.string.guideDetailsText3), modifier = Modifier,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    color = Color.DarkGray
                                )
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            guide.specialPermissions.forEach { permission ->
                                Text(
                                    text = permission,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color(0xFF67282D)
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        //Functionality Information
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = localizedContext.getString(R.string.guideDetailsText4), modifier = Modifier,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    color = Color.DarkGray
                                )
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            if (guide.functionality) {
                                Text(
                                    text = localizedContext.getString(R.string.guideDetailsText5), modifier = Modifier,
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF67282D)
                                    )
                                )
                            } else {
                                Text(
                                    text = localizedContext.getString(R.string.guideDetailsText6), modifier = Modifier,
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF67282D)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
