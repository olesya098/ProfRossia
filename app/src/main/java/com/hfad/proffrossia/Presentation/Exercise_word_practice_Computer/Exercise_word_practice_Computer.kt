package com.hfad.proffrossia.Presentation.Exercise_word_practice_Computer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.hfad.proffrossia.ui.theme.deepblue
import com.hfad.proffrossia.ui.theme.greyDark
import com.hfad.proffrossia.ui.theme.mainusers
import com.hfad.proffrossia.utils.NetworkUtils
import kotlin.random.Random
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Exercise_word_practice_Computer(navController: NavController) {
    val context = LocalContext.current
    val textStyle = FontFamily(Font(R.font.fredokaregular))
    var gameState by remember { mutableStateOf(GameState.PLAYING_WITH_AI) }
    val roomId = remember { generateRoomId() }

    Scaffold(
        topBar = {
            GameTopBar(navController, roomId, textStyle)
        },
        content = { paddingValues ->
            GameScreen(
                paddingValues = paddingValues,
                gameState = gameState,
                textStyle = textStyle
            )
        }
    )
}

enum class GameState {
    PLAYING_WITH_AI,
    PLAYING_WITH_HUMAN
}

fun generateRoomId(): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return (1..6).map { chars.random() }.joinToString("")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopBar(navController: NavController, roomId: String, textStyle: FontFamily) {
    val context = LocalContext.current

    TopAppBar(
        title = {
            Text("Комната: $roomId", color = Color.White, fontFamily = textStyle)
        },
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
}

@Composable
fun GameScreen(paddingValues: PaddingValues, gameState: GameState, textStyle: FontFamily) {
    val englishWords = listOf(
        "apple", "banana", "cat", "dog", "house",
        "tree", "book", "water", "sun", "moon"
    )
    val transcriptions = listOf(
        "[ˈæpəl]", "[bəˈnɑːnə]", "[kæt]", "[dɒɡ]", "[haʊs]",
        "[triː]", "[bʊk]", "[ˈwɔːtər]", "[sʌn]", "[muːn]"
    )
    val translationOptions = listOf(
        listOf("яблоко", "апельсин", "груша", "киви"),
        listOf("банан", "ананас", "киви", "яблоко"),
        listOf("кошка", "собака", "мышь", "волк"),
        listOf("собака", "волк", "лиса", "мышь"),
        listOf("дом", "квартира", "офис", "поляна"),
        listOf("дерево", "цветок", "куст", "ананас"),
        listOf("книга", "журнал", "тетрадь", "статья"),
        listOf("вода", "сок", "молоко", "кола"),
        listOf("солнце", "луна", "звезда", "планета"),
        listOf("луна", "солнце", "планета", "звезда")
    )

    var currentIndex by remember { mutableStateOf(Random.nextInt(englishWords.size)) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isAnswerChecked by remember { mutableStateOf(false) }
    var playerScore by remember { mutableStateOf(0) }
    var opponentScore by remember { mutableStateOf(0) }
    var roundWinner by remember { mutableStateOf<RoundWinner?>(null) }
    var opponentSelectedOption by remember { mutableStateOf<String?>(null) }
    var showCorrectAnswer by remember { mutableStateOf(false) }

    val currentWord = englishWords[currentIndex]
    val currentTranscription = transcriptions[currentIndex]
    val currentOptions = translationOptions[currentIndex]
    val correctAnswer = getCorrectAnswer(currentWord)

    fun updateScores(isPlayerCorrect: Boolean, isOpponentCorrect: Boolean) {
        if (isPlayerCorrect) playerScore++
        if (isOpponentCorrect) opponentScore++
    }

    fun setRoundWinner(winner: RoundWinner) {
        roundWinner = winner
    }
    fun checkAnswers() {
        if (selectedOption == null || opponentSelectedOption == null) return

        val isPlayerCorrect = selectedOption == correctAnswer
        val isOpponentCorrect = opponentSelectedOption == correctAnswer

        updateScores(isPlayerCorrect, isOpponentCorrect)

        roundWinner = when {
            isPlayerCorrect && !isOpponentCorrect -> RoundWinner.PLAYER
            !isPlayerCorrect && isOpponentCorrect -> RoundWinner.OPPONENT
            isPlayerCorrect && isOpponentCorrect -> RoundWinner.BOTH
            else -> {
                showCorrectAnswer = true
                RoundWinner.NONE
            }
        }

        isAnswerChecked = true
    }
    LaunchedEffect(currentIndex) {
        if (gameState == GameState.PLAYING_WITH_AI) {
            delay(3000) // Даем игроку 3 секунды на выбор

            if (!isAnswerChecked) {
                // Компьютер выбирает случайный вариант
                opponentSelectedOption = currentOptions.random()

                // Если игрок не выбрал, считаем его ответ неверным
                if (selectedOption == null) {
                    selectedOption = "" // Пустая строка как признак отсутствия выбора
                }

                checkAnswers()
            }
        }
    }


    fun nextQuestion() {
        currentIndex = (currentIndex + 1) % englishWords.size
        selectedOption = null
        opponentSelectedOption = null
        isAnswerChecked = false
        roundWinner = null
        showCorrectAnswer = false
    }

    val gradient = Brush.verticalGradient(
        colors = List(9) { blue.copy(alpha = if (it == 0) 0.6f else 1f) }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(if (isSystemInDarkTheme()) Darkteam else Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("You", fontFamily = textStyle)
                Text(playerScore.toString(), fontSize = 24.sp, fontFamily = textStyle)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "P2",
                    fontFamily = textStyle
                )
                Text(opponentScore.toString(), fontSize = 24.sp, fontFamily = textStyle)
            }
        }

        Text(
            text = currentWord,
            fontSize = 32.sp,
            fontFamily = textStyle,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)
        )

        Text(
            text = currentTranscription,
            fontSize = 16.sp,
            fontFamily = textStyle,
            modifier = Modifier.padding(bottom = 30.dp)
        )

        currentOptions.forEach { option ->
            val isCorrect = option == correctAnswer
            val isSelected = option == selectedOption
            val isOpponentSelected = option == opponentSelectedOption

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(vertical = 8.dp)
                    .background(
                        when {
                            isAnswerChecked && isCorrect -> Color.Green.copy(alpha = 0.3f)
                            isAnswerChecked && isSelected && !isCorrect -> Color.Red.copy(alpha = 0.3f)
                            isAnswerChecked && isOpponentSelected && !isCorrect -> Color.Red.copy(alpha = 0.3f)
                            isSelected -> blue.copy(alpha = 0.6f)
                            isOpponentSelected -> blue.copy(alpha = 0.6f)
                            isSystemInDarkTheme() -> greyDark
                            else -> mainusers
                        },
                        RoundedCornerShape(15.dp)
                    )
                    .clickable(enabled = !isAnswerChecked && opponentSelectedOption == null) {
                        selectedOption = option
                    },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        fontSize = 18.sp,
                        fontFamily = textStyle,
                        modifier = Modifier.padding(horizontal = 40.dp)
                    )

                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 12.dp)
                                .background(blue, RoundedCornerShape(4.dp))
                        ) {
                            Text(
                                text = "You",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontFamily = textStyle,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    if (isOpponentSelected) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 12.dp)
                                .background(blue, RoundedCornerShape(4.dp))
                        ) {
                            Text(
                                text = "P2",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontFamily = textStyle,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(vertical = 20.dp)
                .background(gradient, RoundedCornerShape(15.dp))
                .clickable {
                    if (!isAnswerChecked && selectedOption != null && opponentSelectedOption != null) {
                        checkAnswers()
                    } else if (isAnswerChecked) {
                        nextQuestion()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isAnswerChecked) "Далее" else "Проверить",
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = textStyle,
                fontWeight = FontWeight.Bold
            )
        }


    }
}

fun getCorrectAnswer(word: String): String {
    return when (word) {
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
}

enum class RoundWinner {
    PLAYER,
    OPPONENT,
    BOTH,
    NONE
}