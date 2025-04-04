package com.hfad.proffrossia.Presentation.PhotoResize

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.hfad.proffrossia.Presentation.navigation.Screen
import com.hfad.proffrossia.R
import com.hfad.proffrossia.ui.theme.Darkteam
import com.hfad.proffrossia.ui.theme.blue
import com.hfad.proffrossia.ui.theme.deepblue
import com.hfad.proffrossia.utils.NetworkUtils
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoResize(navController: NavController, imageUri: String) {
    // Декодирование URI изображения из строки
    val decodedUri = Uri.decode(imageUri)
    // Парсинг URI
    val uri = Uri.parse(decodedUri)
    // Получение контекста
    val context = LocalContext.current
    // Установка стиля шрифта
    val textstyle = FontFamily(Font(R.font.fredokaregular))

    // Состояние для центра круга (изначально в нулевой точке)
    var circleCenter by remember { mutableStateOf(Offset.Zero) }
    // Состояние для радиуса круга (изначально 0)
    var circleRadius by remember { mutableStateOf(0f) }
    // Состояние для хранения bitmap изображения
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

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
    LaunchedEffect(uri) {
        bitmap = try {
            // 1. Создаём источник изображения из URI через ContentResolver
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            // 2. Декодируем изображение в Bitmap
            ImageDecoder.decodeBitmap(source)
        } catch (e: Exception) {
            // В случае ошибки выводим её в лог и возвращаем null
            e.printStackTrace()
            null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Your photo is gorgeous!",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFamily = textstyle,
                        fontSize = 22.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = deepblue
                )
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isSystemInDarkTheme()) Darkteam else Color.White)
                    .padding(bottom = 36.dp),
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
                                navController.navigate(Screen.Profile.route)
                            } else {
                                navController.navigate(Screen.NoConnection.route)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Use that image",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = textstyle,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            // Проверка, что bitmap загружен
            if (bitmap != null) {
                // Создание painter для изображения
                val imagePainter = rememberAsyncImagePainter(model = uri)

                Box(modifier = Modifier.fillMaxSize()) {
                    // 1. Основное изображение
                    Image(
                        painter = imagePainter,
                        contentDescription = "Image to crop",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )

                    // 2. Оверлей с круглой вырезанной областью
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            // Обработка жестов перетаскивания для перемещения круга
                            .pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    circleCenter += dragAmount
                                }
                            }
                    ) {
                        // Получение размеров холста
                        val canvasWidth = size.width
                        val canvasHeight = size.height
                        // Установка радиуса круга (40% от меньшей стороны)
                        circleRadius = min(canvasWidth, canvasHeight) * 0.4f
                        // Инициализация центра круга, если он еще не установлен
                        if (circleCenter == Offset.Zero) {
                            circleCenter = Offset(canvasWidth / 2, canvasHeight / 2)
                        }

                        // Создаем Path для всего холста
                        val overlayPath = Path().apply {
                            addRect(Rect(Offset.Zero, Size(canvasWidth, canvasHeight)))
                        }

                        // Создаем Path для круга (который будет вырезан)
                        val circlePath = Path().apply {
                            addOval(
                                Rect(
                                    center = circleCenter,
                                    radius = circleRadius
                                )
                            )
                        }

                        // Применяем разницу между двумя Path
                        val finalPath = Path.combine(
                            PathOperation.Difference,
                            overlayPath,
                            circlePath
                        )

                        // Рисуем результат с полупрозрачным черным цветом
                        drawPath(
                            path = finalPath,
                            color = Color.Black.copy(alpha = 0.5f)
                        )

                        // Рисуем контур круга
                        drawCircle(
                            color = Color.White,
                            center = circleCenter,
                            radius = circleRadius,
                            style = Stroke(width = 2.dp.toPx())
                        )

                        // Рисуем угловые маркеры (4 белых уголка вокруг круга)
                        val markerLength = 30.dp.toPx()
                        val markerWidth = 4.dp.toPx()

                        // Позиции углов квадрата, описывающего круг
                        val left = circleCenter.x - circleRadius
                        val top = circleCenter.y - circleRadius
                        val right = circleCenter.x + circleRadius
                        val bottom = circleCenter.y + circleRadius

                        // Левый верхний угол
                        drawLine(
                            color = Color.White,
                            start = Offset(left, top),
                            end = Offset(left + markerLength, top),
                            strokeWidth = markerWidth
                        )
                        drawLine(
                            color = Color.White,
                            start = Offset(left, top),
                            end = Offset(left, top + markerLength),
                            strokeWidth = markerWidth
                        )

                        // Правый верхний угол
                        drawLine(
                            color = Color.White,
                            start = Offset(right, top),
                            end = Offset(right - markerLength, top),
                            strokeWidth = markerWidth
                        )
                        drawLine(
                            color = Color.White,
                            start = Offset(right, top),
                            end = Offset(right, top + markerLength),
                            strokeWidth = markerWidth
                        )

                        // Левый нижний угол
                        drawLine(
                            color = Color.White,
                            start = Offset(left, bottom),
                            end = Offset(left + markerLength, bottom),
                            strokeWidth = markerWidth
                        )
                        drawLine(
                            color = Color.White,
                            start = Offset(left, bottom),
                            end = Offset(left, bottom - markerLength),
                            strokeWidth = markerWidth
                        )

                        // Правый нижний угол
                        drawLine(
                            color = Color.White,
                            start = Offset(right, bottom),
                            end = Offset(right - markerLength, bottom),
                            strokeWidth = markerWidth
                        )
                        drawLine(
                            color = Color.White,
                            start = Offset(right, bottom),
                            end = Offset(right, bottom - markerLength),
                            strokeWidth = markerWidth
                        )
                    }

                    // 3. Текстовая подсказка (добавлена ПОСЛЕ Canvas, чтобы быть поверх всего)
                    Text(
                        text = "Just resize that photo\nfor fit in square",
                        color = Color.White,
                        fontFamily = textstyle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        lineHeight = 30.sp,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart)
                    )
                }
            }
        }
    }
}