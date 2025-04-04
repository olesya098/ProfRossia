package com.hfad.proffrossia.Presentation.Exercise_animals.Exercise_animals_success

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hfad.proffrossia.Presentation.navigation.Screen
import com.hfad.proffrossia.R
import com.hfad.proffrossia.ui.theme.Darkteam
import com.hfad.proffrossia.ui.theme.blue
import com.hfad.proffrossia.ui.theme.card4
import com.hfad.proffrossia.ui.theme.deepblue
import com.hfad.proffrossia.utils.NetworkUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Exercise_animals_success(navController: NavController) {
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
    val textstyle = FontFamily(Font(R.font.fredokaregular))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Guess the animal",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFamily = textstyle,
                        fontSize = 22.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    card4
                ),
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.strelka),
                        contentDescription = null,
                        modifier = Modifier
                            .size(37.dp)
                            .padding(start = 10.dp, end = 10.dp)
                            .clickable {
                                if(NetworkUtils.isInternetAvailable(context)){
                                    navController.navigate(Screen.MainScreen.route)
                                }else{
                                    navController.navigate(Screen.NoConnection.route)

                                }
                            }
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(if (isSystemInDarkTheme()) Darkteam else Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 20.dp, top = 60.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.verno),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp).padding(bottom = 40.dp)
                )
                Text(
                    text = "Holy Molly! That is Right!",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontFamily = textstyle,
                    modifier = Modifier.padding(bottom = 20.dp),
                    fontSize = 20.sp
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp)
                        .height(56.dp)
                        .background(gradient, shape = RoundedCornerShape(15.dp))
                        .clickable {

                            if (NetworkUtils.isInternetAvailable(context)) {
                                navController.navigate(Screen.Exercise_animals.route)
                            } else {
                                navController.navigate(Screen.NoConnection.route)
                            }


                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Next",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = textstyle,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier

                    )
                }
            }
        }
    }
}
