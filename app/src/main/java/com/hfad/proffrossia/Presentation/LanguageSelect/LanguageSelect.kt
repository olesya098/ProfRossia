package com.hfad.proffrossia.Presentation.LanguageSelect

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.hfad.proffrossia.Presentation.navigation.Screen
import com.hfad.proffrossia.R
import com.hfad.proffrossia.ui.theme.Darkteam
import com.hfad.proffrossia.ui.theme.blue
import com.hfad.proffrossia.ui.theme.deepblue
import com.hfad.proffrossia.ui.theme.orange
import com.hfad.proffrossia.ui.theme.orangeLite
import com.hfad.proffrossia.utils.NetworkUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelect(navController: NavController) {
    val context = LocalContext.current


    var selectedLanguage by remember { mutableStateOf("") }

    var showError by remember { mutableStateOf(false) }

    val languages = listOf("Russian", "English", "Chinese", "Belarusian", "Kazakh")
    val fredokaFont = FontFamily(Font(R.font.fredokaregular))

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
                title = { /* Пустой заголовок */ },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = deepblue
                ),
                actions = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Language select",
                            textAlign = TextAlign.Center,
                            fontFamily = fredokaFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.White
                        )
                    }
                },

                )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(if (isSystemInDarkTheme()) Darkteam else Color.White),

                ) {
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
                            if (selectedLanguage.isNotEmpty()) {
                                if (NetworkUtils.isInternetAvailable(context)) {
                                    navController.navigate(Screen.Login.route)
                                } else {
                                    navController.navigate(Screen.NoConnection.route)
                                }
                            } else {
                                showError = true
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Continue",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = fredokaFont,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(if (isSystemInDarkTheme()) Darkteam else Color.White)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = "What is your Mother language?",
                fontFamily = fredokaFont,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp),
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
            )
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                if (showError && selectedLanguage.isEmpty()) {
                    Text(
                        text = "Please select a language",
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                languages.forEach { language ->
                    val isSelected = language == selectedLanguage
                    val boxColor = if (isSelected) orange else orangeLite


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .height(67.dp)
                            .background(
                                boxColor,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable {
                                selectedLanguage = language
                                showError = false
                            },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = language,
                            fontFamily = fredokaFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(start = 10.dp),
                            color = Color.Black
                        )
                    }
                }
            }

        }
    }
}

@PreviewScreenSizes
@Composable
fun LanguageSelectPreview() {
    LanguageSelect(rememberNavController())
}