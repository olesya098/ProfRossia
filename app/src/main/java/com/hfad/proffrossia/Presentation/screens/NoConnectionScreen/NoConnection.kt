package com.hfad.proffrossia.Presentation.screens.NoConnectionScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hfad.proffrossia.utils.NetworkUtils
import com.hfad.proffrossia.Presentation.navigation.Screen
import com.hfad.proffrossia.R
import com.hfad.proffrossia.ui.theme.blue
import com.hfad.proffrossia.ui.theme.deepblue
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoConnection(navController: NavController) {
    val fredoreg = FontFamily(Font(R.font.fredokaregular))
    val context = LocalContext.current
    val gradient = Brush.verticalGradient(
        colors = listOf(
            blue.copy(alpha = 0.6f),
            blue,
            blue,
            blue,
            blue,
            blue,
            blue,
            blue,
            blue
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = deepblue
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 60.dp)
                    .height(56.dp)
                    .background(
                        gradient,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .clickable {
                        if (NetworkUtils.isInternetAvailable(context)) {
                            // Возвращаемся назад на предыдущий экран
                            navController.popBackStack()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Check again",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = fredoreg,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "\uD83D\uDE1E",
                    fontSize = 120.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = "No \ninternet connection",
                    fontFamily = fredoreg,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 30.sp,
                    fontSize = 30.sp
                )
            }
        }
    }
}
@PreviewScreenSizes
@Composable
fun PreviewInternet(){
    NoConnection(rememberNavController())
}