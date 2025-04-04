package com.hfad.proffrossia

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hfad.proffrossia.Presentation.navigation.Screen
import com.hfad.proffrossia.ui.theme.deepblue
import com.hfad.proffrossia.utils.NetworkUtils
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    val fredo = FontFamily(Font(R.font.fredokabold))

    LaunchedEffect(Unit) {
        delay(3000L)
        if (NetworkUtils.isInternetAvailable(context)) {
            navController.navigate(Screen.Onboarding.route) {
                popUpTo(Screen.SplashScreen.route) { inclusive = true }
            }
        } else {
            navController.navigate(Screen.NoConnection.route) {
                popUpTo(Screen.SplashScreen.route) { inclusive = true }
            }
        }
    }
    Box (
        modifier = Modifier.fillMaxSize()
            .background(deepblue),
        contentAlignment = Alignment.Center
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.illustrations),
                contentDescription = null,
                modifier = Modifier.size(160.dp)
            )
            Text(
                text = "Language App",
                fontFamily = fredo,
                fontSize = 36.sp,
                color = Color.White
            )

        }

    }
}
@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen(){
    SplashScreen(rememberNavController())
}