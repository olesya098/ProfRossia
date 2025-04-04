package com.hfad.proffrossia.Presentation.Exercise_animals

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.emptyCacheFontFamilyResolver
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hfad.proffrossia.Presentation.navigation.Screen
import com.hfad.proffrossia.R
import com.hfad.proffrossia.ui.theme.Darkteam
import com.hfad.proffrossia.ui.theme.blue
import com.hfad.proffrossia.ui.theme.deepblue
import com.hfad.proffrossia.ui.theme.greyDark
import com.hfad.proffrossia.ui.theme.greyLite
import com.hfad.proffrossia.ui.theme.greyMedium
import com.hfad.proffrossia.ui.theme.greylabel
import com.hfad.proffrossia.utils.NetworkUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Exercise_animals(navController: NavController) {
    val context = LocalContext.current
    val textstyle = FontFamily(Font(R.font.fredokaregular))
    var text by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }
    var errortext by remember { mutableStateOf("") }
    var consecutiveSuccesses = remember { mutableStateOf(0) }
    var totalPoints = remember { mutableStateOf(0) }

    if (error) {
        AlertDialog(
            onDismissRequest = { error = false },
            title = {
                Text(
                    text = "Ошибка",
                    fontFamily = textstyle,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = errortext,
                    fontFamily = textstyle
                )
            },
            confirmButton = {
                Button(
                    onClick = { error = false },
                    colors = ButtonDefaults.buttonColors(containerColor = blue)
                ) {
                    Text(
                        text = "OK",
                        fontFamily = textstyle,
                        color = Color.White
                    )
                }
            }
        )
    }
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
                title = {
                    Text(
                        text = "Guess the animal",
                        color = Color.White,
                        fontFamily = textstyle,
                        fontSize = 22.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    deepblue
                ),
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.strelka),
                        contentDescription = null,
                        modifier = Modifier
                            .size(37.dp)
                            .padding(start = 10.dp, end = 10.dp)
                            .clickable {
                                if (NetworkUtils.isInternetAvailable(context)){
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
                    .padding(start = 20.dp, end = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(328.dp)
                        .background(
                            Color.Transparent,
                            shape = RoundedCornerShape(15.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.raccoon),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .size(328.dp)
                            .background(
                                Color.Transparent,
                                shape = RoundedCornerShape(15.dp)
                            )
                    )

                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {

                    Text(
                        text = "Write who is on image",
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = greylabel,
                        fontFamily = textstyle,
                        textAlign = TextAlign.Start,
                        fontSize = 15.sp
                    )
                }
                TextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = if (isSystemInDarkTheme()) greyDark else greyLite,
                        unfocusedContainerColor = if (isSystemInDarkTheme()) greyDark else greyLite,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        focusedLabelColor = if (isSystemInDarkTheme()) Color.LightGray else greyMedium,
                        unfocusedLabelColor = if (isSystemInDarkTheme()) Color.LightGray else greyMedium,
                        cursorColor = if (isSystemInDarkTheme()) Color.White else Color.Black
                    ),
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp)
                        .height(56.dp)
                        .background(gradient, shape = RoundedCornerShape(15.dp))
                        .clickable {
                            if (text.isBlank()) {
                                error = true
                                errortext = "Поле пустое"
                            } else {
                                if (text == "raccoon") {
                                    // Начисляем базовый балл
                                    totalPoints.value += 1

                                    // Увеличиваем счетчик успешных попыток подряд
                                    consecutiveSuccesses.value += 1

                                    // Если 2 или более успешных попыток подряд, добавляем бонус
                                    if (consecutiveSuccesses.value >= 2) {
                                        val bonus = (0.2 * consecutiveSuccesses.value).toInt()
                                        totalPoints.value += bonus
                                    }

                                    if (NetworkUtils.isInternetAvailable(context)) {
                                        navController.navigate(Screen.Exercise_animals_success.route)
                                    } else {
                                        navController.navigate(Screen.NoConnection.route)
                                    }
                                } else {
                                    // Сбрасываем счетчик успешных попыток при ошибке
                                    consecutiveSuccesses.value = 0
                                    navController.navigate(Screen.Exercise_animals_error.route)
                                }

                            }

                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Check",
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