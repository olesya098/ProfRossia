package com.hfad.proffrossia.Presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hfad.proffrossia.Presentation.Exercise_Listening.Exercise_Listening
import com.hfad.proffrossia.Presentation.Exercise_animals.Exercise_animals
import com.hfad.proffrossia.Presentation.Exercise_animals.Exercise_animals_error.Exercise_animals_error
import com.hfad.proffrossia.Presentation.Exercise_animals.Exercise_animals_success.Exercise_animals_success
import com.hfad.proffrossia.Presentation.Exercise_word_practice.Exercise_word_practice
import com.hfad.proffrossia.Presentation.Exercise_word_practice_Computer.Exercise_word_practice_Computer
import com.hfad.proffrossia.Presentation.LanguageSelect.LanguageSelect
import com.hfad.proffrossia.Presentation.PhotoResize.PhotoResize
import com.hfad.proffrossia.Presentation.Profile.Profile
import com.hfad.proffrossia.Presentation.screens.NoConnectionScreen.NoConnection
import com.hfad.proffrossia.Presentation.screens.auth.Login
import com.hfad.proffrossia.Presentation.screens.auth.Signup
import com.hfad.proffrossia.Presentation.screens.auth.Signup2
import com.hfad.proffrossia.Presentation.screens.main.MainScreen
import com.hfad.proffrossia.Presentation.screens.onboarding.Onboarding
import com.hfad.proffrossia.SplashScreen
import com.hfad.proffrossia.utils.NetworkUtils

@Composable
fun Navigatia() {
    val navController = rememberNavController()
    val context = LocalContext.current

    LaunchedEffect (Unit){
        if (NetworkUtils.isInternetAvailable(context)){
            navController.navigate(Screen.SplashScreen.route)
        } else{
            navController.navigate(Screen.NoConnection.route)
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ) {
        composable(Screen.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(Screen.Onboarding.route) {
            Onboarding(navController)
        }
        composable(Screen.NoConnection.route) {
            NoConnection(navController)
        }
        composable(Screen.LanguageSelect.route) {
            LanguageSelect(navController)
        }
        composable(Screen.Login.route) {
            Login(navController)
        }
        composable(Screen.Signup.route) {
            Signup(navController)
        }
        composable(Screen.Signup2.route) {
            Signup2(navController)
        }
        composable(Screen.MainScreen.route) {
            MainScreen(navController)
        }
        composable(Screen.Profile.route) {
            Profile(navController)
        }
        composable(Screen.Exercise_animals.route) {
            Exercise_animals(navController)
        }
        composable(Screen.Exercise_animals_success.route) {
            Exercise_animals_success(navController)
        }
        composable(Screen.Exercise_animals_error.route) {
            Exercise_animals_error(navController)
        }
        composable(Screen.Exercise_word_practice.route) {
            Exercise_word_practice(navController)
        }
        composable(Screen.Exercise_Listening.route) {
            Exercise_Listening(navController)
        }
        composable(Screen.Exercise_word_practice_Computer.route) {
            Exercise_word_practice_Computer(navController)
        }
        composable(
            route = Screen.PhotoResize.route,
            // Определяем аргументы, которые принимает этот экран
            arguments = listOf(
                navArgument("imageUri") {  // Создаём навигационный аргумент с именем "imageUri"
                    type = NavType.StringType  // Указываем, что это аргумент строкового типа
                }
            )
        ) { backStackEntry ->  // Лямбда, которая будет вызвана при переходе на этот экран

            // Получаем переданный аргумент imageUri из параметров навигации
            val imageUri = backStackEntry.arguments?.getString("imageUri") ?: ""

            // Создаём и отображаем наш экран PhotoResize, передавая:
            // 1. Контроллер навигации
            // 2. Полученный URI изображения
            PhotoResize(navController, imageUri)
        }
    }
}