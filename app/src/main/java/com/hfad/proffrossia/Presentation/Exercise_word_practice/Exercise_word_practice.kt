package com.hfad.proffrossia.Presentation.Exercise_word_practice

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hfad.proffrossia.Presentation.navigation.Screen
import com.hfad.proffrossia.R
import com.hfad.proffrossia.ui.theme.Darkteam
import com.hfad.proffrossia.ui.theme.blue
import com.hfad.proffrossia.ui.theme.card4
import com.hfad.proffrossia.ui.theme.deepblue
import com.hfad.proffrossia.ui.theme.greyDark
import com.hfad.proffrossia.ui.theme.mainusers
import com.hfad.proffrossia.ui.theme.orange
import com.hfad.proffrossia.utils.NetworkUtils
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Exercise_word_practice(navController: NavController) {
    // Получаем контекст приложения
    val context = LocalContext.current

    // Устанавливаем шрифт для текста
    val textstyle = FontFamily(Font(R.font.fredokaregular))

    // Состояния для управления диалоговым окном и выбором ответа
    var showErrorDialog by remember { mutableStateOf(false) } // Показывать ли диалог ошибки
    var selectedOption by remember { mutableStateOf<String?>(null) } // Выбранный вариант ответа
    var isAnswerChecked by remember { mutableStateOf(false) } // Проверен ли ответ

    // Система начисления баллов
    var totalScore by remember { mutableStateOf(0f) } // Общее количество баллов
    var currentStreak by remember { mutableStateOf(0) } // Текущая серия правильных ответов
    var lastAnswerCorrect by remember { mutableStateOf(false) } // Был ли последний ответ верным

    // База данных слов и их переводов
    val englishWords = listOf(
        "apple", "banana", "cat", "dog", "house",
        "tree", "book", "water", "sun", "moon"
    )
    val transcriptions = listOf(
        "[ˈæpəl]", "[bəˈnɑːnə]", "[kæt]", "[dɒɡ]", "[haʊs]",
        "[triː]", "[bʊk]", "[ˈwɔːtər]", "[sʌn]", "[muːn]"
    )
    val translationOptions = listOf(
        listOf("яблоко", "апельсин", "груша","киви"),
        listOf("банан", "ананас", "киви","яблоко"),
        listOf("кошка", "собака", "мышь","волк"),
        listOf("собака", "волк", "лиса","мышь"),
        listOf("дом", "квартира", "офис","поляна"),
        listOf("дерево", "цветок", "куст","ананас"),
        listOf("книга", "журнал", "тетрадь","статья"),
        listOf("вода", "сок", "молоко","колла"),
        listOf("солнце", "луна", "звезда","планета"),
        listOf("луна", "солнце", "планета","звезда")
    )

    // Текущее слово и его данные
    var currentIndex by remember { mutableStateOf(Random.nextInt(englishWords.size)) }
    val currentWord = englishWords[currentIndex] // Текущее английское слово
    val currentTranscription = transcriptions[currentIndex] // Транскрипция слова
    val currentOptions = translationOptions[currentIndex] // Варианты перевода

    // Определяем правильный ответ для текущего слова
    val correctAnswer = when (currentWord) {
        "apple" -> "яблоко"
        "banana" -> "банан"
        "cat" -> "кошка"
        "dog" -> "собака"
        "house" -> "дом"
        "tree" -> "дерево"
        "book" -> "книга"
        "water" -> "вода"
        "sun" -> "солнце"
        "moon" -> "луна"
        else -> ""
    }

    // Градиент для выделения выбранных элементов
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
            blue)
    )

    /**
     * Функция для перехода к следующему слову
     * Сбрасывает выбранный вариант и состояние проверки
     */
    fun nextWord() {
        currentIndex = Random.nextInt(englishWords.size) // Выбираем случайное слово
        selectedOption = null // Сбрасываем выбор
        isAnswerChecked = false // Сбрасываем состояние проверки
    }

    /**
     * Функция обработки ответа пользователя
     * Начисляет баллы согласно системе:
     * - 1 балл за каждый правильный ответ
     * - Дополнительно 0.2 балла за каждый правильный ответ в серии
     */
    fun handleAnswer() {
        val isCorrect = selectedOption == correctAnswer // Проверяем правильность ответа

        if (isCorrect) {
            currentStreak++ // Увеличиваем серию правильных ответов
            // Начисляем баллы: 1 базовый + 0.2 за каждый правильный ответ в серии
            totalScore += 1f + (0.2f * (currentStreak - 1))
            lastAnswerCorrect = true
        } else {
            currentStreak = 0 // Сбрасываем серию при ошибке
            lastAnswerCorrect = false
        }

        isAnswerChecked = true // Помечаем ответ как проверенный
    }

    // Диалоговое окно для отображения ошибки при попытке проверить без выбора
    if (showErrorDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showErrorDialog = false }, // При закрытии
            title = { Text("Ошибка") }, // Заголовок
            text = { Text("Пожалуйста, выберите вариант ответа перед проверкой") }, // Текст
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = blue)
                ) {
                    Text("OK") // Кнопка подтверждения
                }
            }
        )
    }

    // Основной макет экрана
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Word practice", color = Color.White) // Название приложения
                        Spacer(modifier = Modifier.weight(1f)) // Разделитель
                        Text("Баллы: ${"%.1f".format(totalScore)}", color = Color.White) // Отображение баллов
                    }
                },
                navigationIcon = {
                    // Кнопка назад
                    Image(
                        painter = painterResource(id = R.drawable.strelka),
                        contentDescription = null,
                        modifier = Modifier
                            .size(37.dp)
                            .padding(start = 10.dp, end = 10.dp)
                            .clickable {
                                // Проверяем интернет соединение перед переходом
                                if(NetworkUtils.isInternetAvailable(context)){
                                    navController.navigate(Screen.MainScreen.route)
                                }else{
                                    navController.navigate(Screen.NoConnection.route)
                                }
                            }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(deepblue) // Цвет панели
            )
        },
        bottomBar = {
            // Нижняя панель с кнопкой проверки/продолжения
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isSystemInDarkTheme()) Darkteam else Color.White)
                    .padding(start = 20.dp, end = 20.dp, bottom = 50.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(gradient, shape = RoundedCornerShape(15.dp)) // Градиентный фон
                        .clickable {
                            if (!isAnswerChecked) {
                                // Если ответ не проверен
                                if (selectedOption == null) {
                                    showErrorDialog = true // Показываем ошибку если не выбран вариант
                                } else {
                                    handleAnswer() // Обрабатываем ответ
                                }
                            } else {
                                nextWord() // Переходим к следующему слову
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Текст кнопки меняется в зависимости от состояния
                    Text(
                        text = if (isAnswerChecked) "Next" else "Check",
                        color = Color.White,
                        fontFamily = textstyle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }
        }
    ) { paddingValues ->
        // Основное содержимое экрана
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(if (isSystemInDarkTheme()) Darkteam else Color.White)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp) // Отступ для нижней панели
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Отображение текущей серии правильных ответов
            if (currentStreak > 0) {
                Text(
                    text = "СериЯ: $currentStreak правильных ответов подряд",
                    color = blue,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = currentWord,
                fontSize = 32.sp,
                fontFamily = textstyle,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 20.dp),
                textAlign = TextAlign.Center
            )

            // Отображение транскрипции слова
            Text(
                text = currentTranscription,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = textstyle,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 50.dp),
                textAlign = TextAlign.Center
            )

            // Список вариантов перевода
            currentOptions.forEach { option ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .then(
                            // Условное применение стилей в зависимости от состояния
                            if (!isAnswerChecked && option == selectedOption) {
                                // Градиент для выбранного варианта
                                Modifier.background(gradient, shape = RoundedCornerShape(15.dp))
                            } else {
                                // Разные цвета для разных состояний ответа
                                Modifier.background(
                                    when {
                                        // Зеленый для правильного ответа
                                        isAnswerChecked && option == correctAnswer -> card4
                                        // Красный для неправильного выбранного ответа
                                        isAnswerChecked && option == selectedOption && option != correctAnswer -> orange
                                        // Темный фон для темной темы
                                        isSystemInDarkTheme() -> Color(0xFF2D2D2D)
                                        // Светлый фон по умолчанию
                                        else -> mainusers
                                    },
                                    RoundedCornerShape(15.dp)
                                )
                            }
                        )
                        .clickable {
                            // Обработка выбора варианта
                            if (!isAnswerChecked) {
                                selectedOption = option
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = option, fontSize = 20.sp) // Текст варианта ответа
                }
            }
            Spacer(
                modifier = Modifier.padding(bottom = 40.dp)
            )
        }
    }
}