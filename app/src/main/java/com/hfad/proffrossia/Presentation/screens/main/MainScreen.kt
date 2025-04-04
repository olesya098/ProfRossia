package com.hfad.proffrossia.Presentation.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hfad.proffrossia.Domain.models.dataUsers
import com.hfad.proffrossia.Presentation.navigation.Screen
import com.hfad.proffrossia.R
import com.hfad.proffrossia.ui.theme.Darkteam
import com.hfad.proffrossia.ui.theme.blue
import com.hfad.proffrossia.ui.theme.card1
import com.hfad.proffrossia.ui.theme.card2
import com.hfad.proffrossia.ui.theme.card3
import com.hfad.proffrossia.ui.theme.card4
import com.hfad.proffrossia.ui.theme.deepblue
import com.hfad.proffrossia.ui.theme.maingrey
import com.hfad.proffrossia.ui.theme.mainusers
import com.hfad.proffrossia.ui.theme.photo
import com.hfad.proffrossia.utils.NetworkUtils
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    val textstyle = FontFamily(Font(R.font.fredokaregular))
    val textstyleBold = FontFamily(Font(R.font.fredokabold))
    val context = LocalContext.current
    val users = listOf(
        dataUsers(
            R.drawable.art,
            "Vincent van Gogh",
            12
        ),
        dataUsers(
            R.drawable.himia,
            "Dmitri Ivanovich Mendeleev",
            10
        ),
        dataUsers(
            R.drawable.drakula,
            "Vlad Tepes ",
            8
        ),

        )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // Наш кастомный заголовок
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(deepblue) // Цвет фона заголовка
                    .padding(vertical = 32.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Серый кружок
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(
                            photo,
                            CircleShape
                        )
                        .clickable {
                            if (NetworkUtils.isInternetAvailable(context)) {
                                navController.navigate(Screen.Profile.route)

                            } else {
                                navController.navigate(Screen.NoConnection.route)
                            }

                        }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Hello, Emil",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = textstyle,
                    fontSize = 22.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Are you ready for learning today?",
                    color = maingrey,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    fontFamily = textstyle,
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(if (isSystemInDarkTheme()) Darkteam else Color.White)
                .padding(horizontal = 16.dp) // Уменьшил отступы по бокам
        ) {
            // Top users section
            Text(
                text = "Top users",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = textstyle,
                modifier = Modifier.padding(top = 10.dp)
            )

            repeat(users.size) { index ->
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .background(mainusers, RoundedCornerShape(15.dp))
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = users[index].photo),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(10.dp)
                                .size(36.dp)

                        )
                        Text(
                            modifier = Modifier.width(150.dp),
                            text = users[index].name,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontFamily = textstyle,
                            fontSize = 17.sp,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            text = "${users[index].point} points",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontFamily = textstyle,
                            modifier = Modifier.padding(end = 12.dp),
                            fontSize = 17.sp,
                        )
                    }
                }
            }

            Text(
                text = "Available exercises",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = textstyle,
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Карточка 1
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp) // Уменьшенная высота
                        .background(card1, RoundedCornerShape(16.dp))
                        .padding(12.dp)
                        .clickable {
                            if (NetworkUtils.isInternetAvailable(context)) {
                                navController.navigate(Screen.Exercise_animals.route)
                            } else {
                                navController.navigate(Screen.NoConnection.route)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.bear),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(90.dp), // Уменьшенный размер изображения
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = "Guess the animal",
                            fontSize = 14.sp,
                            color = Color.White,
                            fontFamily = textstyle,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(0.9f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Карточка 2
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp) // Уменьшенная высота
                        .background(card2, RoundedCornerShape(16.dp))
                        .padding(12.dp)
                        .clickable {
                            if (NetworkUtils.isInternetAvailable(context)) {
                                navController.navigate(Screen.Exercise_word_practice.route)
                            } else {
                                navController.navigate(Screen.NoConnection.route)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.pensil),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(90.dp), // Уменьшенный размер изображения
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = "Word practice",
                            fontSize = 14.sp,
                            color = Color.White,
                            fontFamily = textstyle,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(0.9f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Второй ряд карточек
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Карточка 3
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            if (NetworkUtils.isInternetAvailable(context)) {
                                navController.navigate(Screen.Exercise_Listening.route)
                            } else {
                                navController.navigate(Screen.NoConnection.route)
                            }

                        }
                        .height(150.dp) // Уменьшенная высота
                        .background(card3, RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.audio),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(90.dp), // Уменьшенный размер изображения
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = "Audition",
                            fontSize = 14.sp,
                            color = Color.White,
                            fontFamily = textstyle,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(0.9f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                var showCountdown by remember { mutableStateOf(false) }
                var countdownValue by remember { mutableStateOf(4) }

                LaunchedEffect(showCountdown) {
                    if (showCountdown) {
                        while (countdownValue > 0) {
                            delay(1000)
                            countdownValue--
                        }
                        if (NetworkUtils.isInternetAvailable(context)) {
                            navController.navigate(Screen.Exercise_word_practice_Computer.route)
                        } else {
                            navController.navigate(Screen.NoConnection.route)
                        }
                        showCountdown = false
                        countdownValue = 4 // Сбрасываем счетчик
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            showCountdown = true
                        }
                        .height(150.dp)
                        .background(card4, RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (showCountdown) {
                        // Отображаем обратный отсчет
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(blue, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = countdownValue.toString(),
                                fontSize = 36.sp,
                                color = Color.White,
                                fontFamily = textstyle,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        // Стандартное содержимое карточки
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.game),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .height(90.dp),
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                text = "Game",
                                fontSize = 14.sp,
                                color = Color.White,
                                fontFamily = textstyle,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(0.9f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@PreviewScreenSizes
@Composable
fun MainScreenPreview() {
    MainScreen(rememberNavController())
}