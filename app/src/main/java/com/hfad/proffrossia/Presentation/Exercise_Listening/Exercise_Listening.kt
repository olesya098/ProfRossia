package com.hfad.proffrossia.Presentation.Exercise_Listening

import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.hfad.proffrossia.ui.theme.deepblue
import com.hfad.proffrossia.utils.NetworkUtils
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Exercise_Listening(navController: NavController) {
    val englishWords = listOf(
        "apple", "banana", "cat", "dog", "house",
        "tree", "book", "water", "sun", "moon"
    )

    val transcriptions = listOf(
        "[ˈæpəl]", "[bəˈnɑːnə]", "[kæt]", "[dɒɡ]", "[haʊs]",
        "[triː]", "[bʊk]", "[ˈwɔːtər]", "[sʌn]", "[muːn]"
    )

    val context = LocalContext.current
    val textstyle = FontFamily(Font(R.font.fredokaregular))

    var currentIndex by remember { mutableStateOf(Random.nextInt(englishWords.size)) }
    val currentWord = remember { derivedStateOf { englishWords[currentIndex] } }
    val currentTranscription = remember { derivedStateOf { transcriptions[currentIndex] } }

    var isRecording by remember { mutableStateOf(false) }
    var userAnswer by remember { mutableStateOf("") }
    var isAnswerCorrect by remember { mutableStateOf(false) }
    var showMicrophone by remember { mutableStateOf(false) }
    var speechError by remember { mutableStateOf<String?>(null) }
    var hasRecordPermission by remember { mutableStateOf(false) }

    // Запрос разрешения на запись аудио
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasRecordPermission = isGranted
        if (!isGranted) {
            speechError = "Microphone permission required"
        }
    }

    // Проверяем разрешение при запуске
    LaunchedEffect(Unit) {
        permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
    }

    // Анимация пульсации микрофона
    val pulseValue = remember { Animatable(1f) }
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }

    // Слушатель распознавания речи
    val recognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: android.os.Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() { isRecording = false }

        override fun onError(error: Int) {
            isRecording = false
            speechError = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "Audio error"
                SpeechRecognizer.ERROR_CLIENT -> "Client error"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "No permissions"
                SpeechRecognizer.ERROR_NETWORK -> "Network error"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                SpeechRecognizer.ERROR_NO_MATCH -> "No match found"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer busy"
                SpeechRecognizer.ERROR_SERVER -> "Server error"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech detected"
                else -> "Unknown error: $error"
            }
        }

        override fun onResults(results: android.os.Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (!matches.isNullOrEmpty()) {
                userAnswer = matches[0]
                isAnswerCorrect = isPronunciationCorrect(userAnswer, currentWord.value)
            } else {
                speechError = "Speech not recognized"
            }
            isRecording = false
            showMicrophone = false
        }

        override fun onPartialResults(partialResults: android.os.Bundle?) {}
        override fun onEvent(eventType: Int, params: android.os.Bundle?) {}
    }

    // Управление жизненным циклом SpeechRecognizer
    DisposableEffect(Unit) {
        speechRecognizer.setRecognitionListener(recognitionListener)
        onDispose {
            speechRecognizer.destroy()
        }
    }

    // Анимация пульсации при записи
    LaunchedEffect(isRecording) {
        if (isRecording) {
            pulseValue.animateTo(
                targetValue = 1.3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            pulseValue.stop()
            pulseValue.snapTo(1f)
        }
    }

    val gradient = Brush.verticalGradient(
        colors = List(9) { blue.copy(alpha = if (it == 0) 0.6f else 1f) }
    )

    // Функция для начала записи
    fun startListening() {
        if (!isRecording) {
            if (!hasRecordPermission) {
                speechError = "Microphone permission required"
                permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                return
            }

            if (!SpeechRecognizer.isRecognitionAvailable(context)) {
                speechError = "Speech recognition service not available"
                return
            }

            try {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Say '${currentWord.value}'")
                    putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                }
                speechRecognizer.startListening(intent)
                isRecording = true
                userAnswer = ""
                speechError = null
                isAnswerCorrect = false // Сбрасываем состояние правильности ответа
            } catch (e: Exception) {
                speechError = "Error: ${e.localizedMessage}"
                isRecording = false
            }
        }
    }

    // Функция для перехода к следующему слову
    fun goToNextWord() {
        currentIndex = (currentIndex + 1) % englishWords.size
        userAnswer = ""
        isAnswerCorrect = false
        showMicrophone = false // Важное исправление: сбрасываем состояние микрофона
        speechError = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Word practice", color = Color.White, fontFamily = textstyle) },
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.strelka),
                        contentDescription = null,
                        modifier = Modifier
                            .size(37.dp)
                            .padding(horizontal = 10.dp)
                            .clickable {
                                if (NetworkUtils.isInternetAvailable(context)) {
                                    navController.navigate(Screen.MainScreen.route)
                                } else {
                                    navController.navigate(Screen.NoConnection.route)
                                }
                            }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(deepblue)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isSystemInDarkTheme()) Darkteam else Color.White)
                    .padding(start = 20.dp, end = 20.dp, bottom = 50.dp)
            ) {
                if (showMicrophone) {
                    // Круглый пульсирующий контейнер с микрофоном
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(blue)
                            .align(Alignment.Center)
                            .scale(pulseValue.value),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.microfon),
                            contentDescription = "Record",
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { startListening() }
                        )
                    }
                } else if (isAnswerCorrect) {
                    // Кнопка "Yay! Go next" после правильного ответа
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(gradient, shape = RoundedCornerShape(15.dp))
                            .clickable { goToNextWord() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Yay! Go next",
                            color = Color.White,
                            fontFamily = textstyle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                } else {
                    // Основная кнопка "Check my speech"
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(gradient, shape = RoundedCornerShape(15.dp))
                            .clickable { showMicrophone = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Check my speech",
                            color = Color.White,
                            fontFamily = textstyle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(if (isSystemInDarkTheme()) Darkteam else Color.White)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp)
                .padding(horizontal = 20.dp),
        ) {
            Text(
                text = currentWord.value,
                fontSize = 32.sp,
                fontFamily = textstyle,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = currentTranscription.value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = textstyle,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 50.dp),
                textAlign = TextAlign.Center
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (showMicrophone) "Speak now..." else "Tap the button below to check your pronunciation",
                    fontSize = 22.sp,
                    fontFamily = textstyle,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                if (speechError != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = speechError!!,
                        color = Color.Red,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (userAnswer.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = "You said: $userAnswer",
                        onValueChange = {},
                        readOnly = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = if (isAnswerCorrect) Color.Green.copy(alpha = 0.2f)
                            else Color.Red.copy(alpha = 0.2f),
                            unfocusedContainerColor = if (isAnswerCorrect) Color.Green.copy(alpha = 0.2f)
                            else Color.Red.copy(alpha = 0.2f),
                            disabledTextColor = if (isAnswerCorrect) Color.Green
                            else Color.Red
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isAnswerCorrect) "✓ Correct!" else "✗ Incorrect, try again",
                        color = if (isAnswerCorrect) Color.Green else Color.Red,
                        fontSize = 18.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

fun isPronunciationCorrect(recognized: String, expected: String): Boolean {
    // Приводим к нижнему регистру и удаляем пробелы по краям
    val recognizedClean = recognized.trim().lowercase()
    val expectedClean = expected.trim().lowercase()

    // 1. Точное совпадение (основной случай)
    if (recognizedClean == expectedClean) return true

//    // 2. Проверка на множественное число
//    if (recognizedClean == "${expectedClean}s") return true
//
//    // 3. Проверка на артикли (a/an/the) в начале
//    val recognizedNoArticles = recognizedClean
//        .removePrefix("a ")
//        .removePrefix("an ")
//        .removePrefix("the ")
//        .trim()
//
//    if (recognizedNoArticles == expectedClean) return true

    // 4. Специальные случаи для конкретных слов
    val specialCases = mapOf(
        "apple" to listOf("appel", "aple", "app"),
        "banana" to listOf("banan", "bananna"),
        "tree" to listOf("three"),
        "sun" to listOf("son"),
        "water" to listOf("woter", "wata")
    )

    if (specialCases[expectedClean]?.contains(recognizedClean) == true) return true


    return false
}