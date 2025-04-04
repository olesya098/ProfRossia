package com.hfad.proffrossia.Presentation.Profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.hfad.proffrossia.Presentation.navigation.Screen
import com.hfad.proffrossia.R
import com.hfad.proffrossia.ui.theme.Darkteam
import com.hfad.proffrossia.ui.theme.blue
import com.hfad.proffrossia.ui.theme.card4
import com.hfad.proffrossia.ui.theme.deepblue
import com.hfad.proffrossia.ui.theme.maingrey
import com.hfad.proffrossia.ui.theme.mainusers
import com.hfad.proffrossia.ui.theme.photo
import com.hfad.proffrossia.utils.NetworkUtils

@Composable
fun Profile(navController: NavController) {
    val context = LocalContext.current
    val textstyle = FontFamily(Font(R.font.fredokaregular))
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

    // Состояние для хранения URI выбранного изображения
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher для выбора изображения из галереи
    val imagePicker = rememberLauncherForActivityResult(
        //GetContent() для выбора любого типа контента
        //  (в данном случае будет использоваться для выбора изображений)
        contract = ActivityResultContracts.GetContent(),
        // Обработчик результата (выбранного изображения)
        onResult = { uri ->
            uri?.let {
                // Сохраняем выбранный URI изображения в состоянии
                selectedImageUri = it
                // Используем правильный вызов для навигации
                navController.navigate(
                    // Создаем route с закодированным URI изображения
                    // Метод createRoute генерирует правильный путь для навигации
                    route = Screen.PhotoResize.createRoute(Uri.encode(it.toString())),
                    navOptions = NavOptions.Builder()
                        // Устанавливаем флаг singleTop - если экран уже на вершине стека,
                        // новый экземпляр создаваться не будет
                        .setLaunchSingleTop(true)
                        .build()
                )
            }
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(deepblue)
                    .padding(vertical = 32.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Box(
                    modifier = Modifier
                        .size(134.dp)
                        .background(card4, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    selectedImageUri?.let { uri ->
                        // Используем AsyncImage для загрузки изображения
                        AsyncImage(
                            model = uri,
                            contentDescription = "Profile image",
                            modifier = Modifier.size(90.dp)
                        )
                    } ?: run {
                        // Стандартное изображение, если ничего не выбрано
                        Image(
                            painter = painterResource(id = R.drawable.programmist),
                            contentDescription = "Default profile image",
                            modifier = Modifier.size(90.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Your profile, Emil",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = textstyle,
                    fontSize = 22.sp
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isSystemInDarkTheme()) Darkteam else Color.White)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Кнопка 1
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .height(56.dp)
                        .background(gradient, shape = RoundedCornerShape(15.dp))
                        .clickable {
                            if (NetworkUtils.isInternetAvailable(context)) {
                            } else {
                                navController.navigate(Screen.NoConnection.route)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Switch to Dark",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = textstyle,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Кнопка 2
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .height(56.dp)
                        .background(gradient, shape = RoundedCornerShape(15.dp))
                        .clickable {
                            if (NetworkUtils.isInternetAvailable(context)) {
                                navController.navigate(Screen.LanguageSelect.route)
                            } else {
                                navController.navigate(Screen.NoConnection.route)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Change mother language",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = textstyle,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Кнопка 3 - Изменение изображения
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .height(56.dp)
                        .background(gradient, shape = RoundedCornerShape(15.dp))
                        .clickable {
                            if (NetworkUtils.isInternetAvailable(context)) {
                                imagePicker.launch("image/*")

                            } else {
                                navController.navigate(Screen.NoConnection.route)
                            }

                            imagePicker.launch("image/*")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Change your image",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = textstyle,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Кнопка 4
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 30.dp)
                        .height(56.dp)
                        .background(mainusers, shape = RoundedCornerShape(15.dp))
                        .clickable {
                            if (NetworkUtils.isInternetAvailable(context)) {
                            } else {
                                navController.navigate(Screen.NoConnection.route)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Logout",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = textstyle,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues))
    }
}

@PreviewScreenSizes
@Composable
fun ProfilePreview() {
    Profile(rememberNavController())
}