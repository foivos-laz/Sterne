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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import androidx.compose.material.icons.filled.AddIcCall
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PhoneBluetoothSpeaker
import androidx.compose.material.icons.filled.Speaker
import androidx.compose.ui.platform.LocalContext
import com.example.myapp.LocalAppLanguage
import com.example.sterne.R
import com.example.sterne.createLocalizedContext

@Composable
fun AICallScreen(modifier: Modifier = Modifier, navController: NavController) {
    var running by remember { mutableStateOf(true) }
    var elapsedSeconds by remember { mutableStateOf(0L) }

    val language = LocalAppLanguage.current
    val context = LocalContext.current
    val localizedContext = remember(language) { context.createLocalizedContext(language) }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF6E9CF))) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)
            .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            LaunchedEffect(running) {
                // Use a loop that checks isActive so it cancels cleanly with composition lifecycle
                while (running && isActive) {
                    delay(1000L)
                    elapsedSeconds += 1L
                }
            }

            val minutes = (elapsedSeconds / 60) % 60
            val seconds = elapsedSeconds % 60
            val label = String.format("%02d:%02d", minutes, seconds)

            Spacer(modifier = Modifier.height(50.dp))

            Text(text = "Dad <3", style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
                color = Color.Black)

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = label, style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            ),
                color = Color.Black)
        }

        CallControls(
            onEndCall = { navController.popBackStack() } // or your call end logic
        )
    }
}

@Composable
fun ActionCircle(
    icon: ImageVector,
    text: String,
    size: Dp,
    iconSize: Dp,
    backgroundColor: Color,
    onClick: () -> Unit,
    iconTint: Color = Color.White
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(backgroundColor)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = iconTint,
                modifier = Modifier.size(iconSize)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = text, fontSize = 14.sp, color = Color.Black)
    }
}

@Composable
fun CallControls(
    onMute: () -> Unit = {},
    onSpeaker: () -> Unit = {},
    onOutput: () -> Unit = {},
    onKeypad: () -> Unit = {},
    onEndCall: () -> Unit = {}
) {
    val mainColor = Color(0xFF67282D)
    val endCallColor = Color(0xFFA13C43)
    val iconSize = 26.dp
    val circleSize = 70.dp

    val language = LocalAppLanguage.current
    val context = LocalContext.current
    val localizedContext = remember(language) { context.createLocalizedContext(language) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        // Top Row: Speaker - Keypad - Output
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActionCircle(icon = Icons.Default.Speaker, text = localizedContext.getString(R.string.AICallSCRText1), circleSize, iconSize, mainColor, onSpeaker)
            ActionCircle(icon = Icons.Default.Dialpad, text = localizedContext.getString(R.string.AICallSCRText2), circleSize, iconSize, mainColor, onKeypad)
            ActionCircle(icon = Icons.Default.PhoneBluetoothSpeaker, text = localizedContext.getString(R.string.AICallSCRText3), circleSize, iconSize, mainColor, onOutput)
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Bottom Row: Mute - End Call - Add Call
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActionCircle(icon = Icons.Default.MicOff, text = localizedContext.getString(R.string.AICallSCRText4), circleSize, iconSize, mainColor, onMute)

            // End Call in Red Style
            ActionCircle(
                icon = Icons.Default.CallEnd,
                text = localizedContext.getString(R.string.AICallSCRText5),
                size = circleSize,
                iconSize = iconSize,
                backgroundColor = endCallColor,
                onClick = onEndCall,
                iconTint = Color.White
            )

            ActionCircle(icon = Icons.Default.AddIcCall, text = localizedContext.getString(R.string.AICallSCRText6), circleSize, iconSize, mainColor, onOutput)
        }
    }
}