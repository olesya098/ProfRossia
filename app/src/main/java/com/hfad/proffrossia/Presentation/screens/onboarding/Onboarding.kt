package com.hfad.proffrossia.Presentation.screens.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hfad.proffrossia.Domain.models.SliderData
import com.hfad.proffrossia.Presentation.navigation.Screen
import com.hfad.proffrossia.R
import com.hfad.proffrossia.ui.theme.Darkteam
import com.hfad.proffrossia.ui.theme.blue
import com.hfad.proffrossia.ui.theme.grey
import com.hfad.proffrossia.ui.theme.orange
import com.hfad.proffrossia.utils.NetworkUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Onboarding(navController: NavController) {

    val context = LocalContext.current
    val textstyle = FontFamily(Font(R.font.fredokaregular))
    val slider = listOf(
        SliderData(
            R.drawable.slide1,
            "Confidence in your words",
            "With conversation-based learning, you'll be talking from lesson one",
            "Next"
        ),
        SliderData(
            R.drawable.slide2,
            "Take your time to learn",
            "Develop a habit of learning and make it a part of your daily routine",
            "More"
        ),
        SliderData(
            R.drawable.slide3,
            "The lessons you need to learn",
            "Using a variety of learning styles to learn and retain",
            "Choose a language"
        ),
    )
    val pagerState = rememberPagerState(pageCount = { slider.size })
    val coroutine = rememberCoroutineScope()

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

    Surface(modifier = Modifier.fillMaxSize()) {
        val visible = remember {
            List(slider.size) { mutableStateOf(false) }
        }
        LaunchedEffect(pagerState.currentPage) {
            visible.forEach { it.value = false }
            delay(100)
            visible[pagerState.currentPage].value = true
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isSystemInDarkTheme()) Darkteam else Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                val sli = slider[page]
                AnimatedVisibility(
                    visible = visible[page].value,
                    enter = fadeIn(tween(700)),
                    exit = fadeOut(tween(700)),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(top = 50.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // 1. Картинка
                            Image(
                                painter = painterResource(sli.image),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(top = 50.dp)
                                    .size(220.dp)
                            )

                            // 2. Индикаторы
                            Row(
                                modifier = Modifier.padding(top = 70.dp, bottom = 35.dp)
                            ) {
                                repeat(slider.size) { iteration ->
                                    val color = if (pagerState.currentPage == iteration)
                                        orange
                                    else
                                        grey

                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 3.dp)
                                            .size(8.dp)
                                            .background(
                                                color,
                                                shape = CircleShape
                                            )
                                    )
                                }
                            }

                            // 3. Текст
                            Column(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = sli.text1,
                                    fontFamily = textstyle,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                    textAlign = TextAlign.Center
                                )

                                Text(
                                    text = sli.text2,
                                    modifier = Modifier.padding(top = 15.dp),
                                    fontFamily = textstyle,
                                    fontSize = 15.sp,
                                    color = grey,
                                    textAlign = TextAlign.Center
                                )
                            }


                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 30.dp, top = 40.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .background(
                                            gradient,
                                            shape = RoundedCornerShape(15.dp)
                                        )
                                        .clickable {
                                            if (pagerState.currentPage < slider.size - 1) {
                                                coroutine.launch {
                                                    pagerState.scrollToPage(pagerState.currentPage + 1)
                                                }
                                            } else {
                                                if (NetworkUtils.isInternetAvailable(context)) {
                                                    navController.navigate(Screen.LanguageSelect.route)
                                                } else {
                                                    navController.navigate(Screen.NoConnection.route)
                                                }
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = sli.textButton,
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontFamily = textstyle,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Text(
                                    text = "Skip onboarding",
                                    modifier = Modifier
                                        .padding(top = 15.dp)
                                        .clickable {
                                            navController.navigate(Screen.LanguageSelect.route)
                                        },
                                    textAlign = TextAlign.Center,
                                    fontFamily = textstyle,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}