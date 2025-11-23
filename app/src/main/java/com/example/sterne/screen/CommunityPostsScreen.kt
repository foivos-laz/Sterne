package com.example.sterne.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.myapp.LocalAppLanguage
import com.example.sterne.createLocalizedContext
import com.example.sterne.R

@Composable
fun CommunityPostsScreen(modifier: Modifier = Modifier) {
    val language = LocalAppLanguage.current
    val context = LocalContext.current
    val localizedContext = remember(language) { context.createLocalizedContext(language) }

    val scrollState = rememberScrollState()
    var showDialog by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()
        .background(Color(0xFFF6E9CF))){
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Community Posts", style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            ),
                color = Color(0xFF67282D)
            )

            Column(modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
            ){

            }

            Spacer(modifier = Modifier.weight(1f))

            Text(text = "Add your own post by pressing the button below", style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Thin,
                textAlign = TextAlign.Center
            ),
                color = Color(0xFF67282D)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                showDialog = true
            },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67282D)),
                modifier = Modifier.fillMaxWidth()
                    .height(40.dp)
            ){
                Text(text = "Create a Post", style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                ),
                    color = Color(0xFFF6E9CF)
                )
            }
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF67282D),
                tonalElevation = 8.dp,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Create a Post", color = Color(0xFFF6E9CF), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = inputText,
                        onValueChange = { inputText = it},
                        modifier = Modifier.fillMaxWidth(),
                        //singleLine = true,
                        label = {
                            Text(text = "Write your post")
                        },
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
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text= "By pressing 'Upload' your post will be uploaded along with your current location"
                        , color = Color(0xFFF6E9CF), fontSize = 12.sp, textAlign = TextAlign.Center)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center){
                        Button(
                            onClick = {
                                showDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF6E9CF)
                            )
                        ) {
                            Text(text = "Cancel", color = Color(0xFF67282D))
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {

                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF6E9CF)
                            )
                        ) {
                            Text(text = "Upload", color = Color(0xFF67282D))
                        }
                    }
                }
            }
        }
    }
}