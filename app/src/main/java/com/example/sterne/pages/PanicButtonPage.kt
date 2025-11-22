package com.example.sterne.pages

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.myapp.LocalAppLanguage
import com.example.sterne.R
import com.example.sterne.createLocalizedContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.sterne.DataStore.getPhoneNumber
import com.example.sterne.DataStore.savePhoneNumber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PanicButtonPage(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val language = LocalAppLanguage.current
    val localizedContext = remember(language) { context.createLocalizedContext(language) }

    var showDialog by remember { mutableStateOf(false) }

    val phoneNumberFlow = remember { getPhoneNumber(context) }
    val phoneNumber by phoneNumberFlow.collectAsState(initial = "112")

    val callPermission = Manifest.permission.CALL_PHONE

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, perform the desired action
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = Modifier.fillMaxSize()
        .background(Color(0xFFF6E9CF))){
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = localizedContext.getString(R.string.panicPgText1), style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            ),
                color = Color(0xFF67282D)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = localizedContext.getString(R.string.panicPgText2), style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Thin,
                textAlign = TextAlign.Center
            ),
                color = Color(0xFF67282D)
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(text = localizedContext.getString(R.string.panicPgText3), style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Thin,
                textAlign = TextAlign.Center
            ),
                color = Color(0xFF67282D)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(onClick = {
                if (ContextCompat.checkSelfPermission(context, callPermission)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                    context.startActivity(intent)
                } else {
                    permissionLauncher.launch(callPermission)
                }
            },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67282D)),
                shape = CircleShape,
                border = BorderStroke(
                    width = 3.dp,
                    color = Color(0xFF8E3A3F)
                ),
                modifier = Modifier.size(200.dp)
            ){
                Text(text = localizedContext.getString(R.string.panicPgText1), style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                ),
                    color = Color(0xFFF6E9CF)
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(text = localizedContext.getString(R.string.panicPgText4),
                modifier = Modifier.clickable {
                    showDialog = true
                },
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline
                ),
                color = Color(0xFF67282D)
            )

            if (showDialog) {
                var tempNumber by remember { mutableStateOf(phoneNumber) }

                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    containerColor = Color(0xFF67282D),
                    title = {
                        Text(localizedContext.getString(R.string.panicPgDlgText1), fontFamily = FontFamily.Monospace,
                            color = Color(0xFFF6E9CF)
                        )
                    },
                    text = {
                        OutlinedTextField(
                            value = tempNumber,
                            onValueChange = { newValue: String ->
                                tempNumber = newValue.replace(" ", "")
                            },
                            label = { Text(localizedContext.getString(R.string.regBox3)) },
                            singleLine = true,
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
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                savePhoneNumber(context, tempNumber)
                            }
                            showDialog = false }) {
                            Text(localizedContext.getString(R.string.panicPgDlgText2), color = Color(0xFFF6E9CF))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text(localizedContext.getString(R.string.panicPgDlgText3), color = Color(0xFFF6E9CF))
                        }
                    }
                )
            }
        }
    }
}