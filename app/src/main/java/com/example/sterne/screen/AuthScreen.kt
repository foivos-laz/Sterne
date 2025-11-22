package com.example.sterne.screen

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapp.LocalAppLanguage
import com.example.sterne.R
import com.example.sterne.createLocalizedContext
import java.util.Locale

@Composable
fun AuthScreen(modifier: Modifier = Modifier, navController: NavController, onToggleLanguage: () -> Unit) {

    var context = LocalContext.current
    val language = LocalAppLanguage.current

    val localizedContext = remember(language) {
        val config = Configuration(context.resources.configuration)
        config.setLocale(Locale.forLanguageTag(language))
        context.createConfigurationContext(config)
    }
    Column(modifier = Modifier.fillMaxSize()
        .background(Color(0xFF67282D))){
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painter = painterResource(id = R.drawable.hera_logo_transparent_beige_preview), contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp))

            //Spacer(modifier = Modifier.height(10.dp))

            Text(text = localizedContext.getString(R.string.authText1), style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
                ),
                color = Color(0xFFF6E9CF)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = localizedContext.getString(R.string.authText2), style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Thin,
                textAlign = TextAlign.Center
            ),
                color = Color(0xFFF6E9CF)
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedButton(onClick = {
                navController.navigate("login")
            },
                border = BorderStroke(1.dp, Color(0xFFF6E9CF)),
                modifier = Modifier.fillMaxWidth()
                    .height(60.dp)
                ){
                Text(text = localizedContext.getString(R.string.authButton2), style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                ),
                    color = Color(0xFFF6E9CF)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                navController.navigate("signup")
            },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF6E9CF)),
                    modifier = Modifier.fillMaxWidth()
                    .height(60.dp)
            ){
                Text(text = localizedContext.getString(R.string.authButton1), style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                ),
                    color = Color(0xFF67282D)
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Text(text = localizedContext.getString(R.string.authText3),
                modifier = Modifier.clickable {
                    onToggleLanguage()
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