package com.example.sterne.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.myapp.LocalAppLanguage
import com.example.sterne.createLocalizedContext
import com.example.sterne.pages.AISpeakPage
import com.example.sterne.pages.HomePage
import com.example.sterne.pages.LocationServices
import com.example.sterne.pages.PanicButtonPage
import com.example.sterne.R

@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavController) {

    val language = LocalAppLanguage.current
    val context = LocalContext.current
    val localizedContext = remember(language) { context.createLocalizedContext(language) }

    val navItemList = listOf(
        NavItem(localizedContext.getString(R.string.homeSCRText1), Icons.Default.Home),
        NavItem(localizedContext.getString(R.string.homeSCRText2), Icons.Default.Phone),
        NavItem(localizedContext.getString(R.string.homeSCRText3), Icons.Default.Warning),
        NavItem(localizedContext.getString(R.string.homeSCRText4), Icons.Default.LocationOn),
    )

    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(bottomBar = {
        NavigationBar(
            containerColor = Color(0xFF67282D)
        ) {
            navItemList.forEachIndexed { index, navItem ->
                NavigationBarItem(selected = index == selectedIndex,
                    onClick = {
                        selectedIndex = index
                    },
                    icon = {
                        Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                    },
                    label = {
                        Text(text = navItem.label)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFFF6E9CF),
                        selectedTextColor = Color(0xFFF6E9CF),
                        unselectedIconColor = Color(0xFFF6E9CF),
                        unselectedTextColor = Color(0xFFF6E9CF),
                        indicatorColor = Color(0xFFA13C43)
                    )) }
        }
    }) {
        ContentScreen(modifier = Modifier.padding(it), selectedIndex, navController)
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int, navController : NavController) {
    when (selectedIndex) {
        0 -> HomePage(modifier, navController)
        1 -> AISpeakPage(modifier, navController)
        2 -> PanicButtonPage(modifier, navController)
        3 -> LocationServices(modifier, navController)
    }
}

data class NavItem(
    val label: String,
    val icon: ImageVector
)