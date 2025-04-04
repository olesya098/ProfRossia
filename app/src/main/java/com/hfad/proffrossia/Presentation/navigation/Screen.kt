package com.hfad.proffrossia.Presentation.navigation

sealed class Screen(val route: String){
    object SplashScreen: Screen("SplashScreen")
    object Onboarding: Screen("Onboarding")
    object NoConnection: Screen("NoConnection")
    object LanguageSelect: Screen("LanguageSelect")
    object Login: Screen("Login")
    object Signup: Screen("Signup")
    object Signup2: Screen("Signup2")
    object MainScreen: Screen("MainScreen")
    object Exercise_word_practice_Computer: Screen("Exercise_word_practice_Computer")
    object Profile: Screen("Profile")
    object Exercise_Listening: Screen("Exercise_Listening")
    object Exercise_word_practice: Screen("Exercise_word_practice")

    object Exercise_animals: Screen("Exercise_animals")
    object Exercise_animals_error: Screen("Exercise_animals_error")
    object Exercise_animals_success: Screen("Exercise_animals_success")
    object PhotoResize : Screen("photo_resize/{imageUri}") {
        fun createRoute(imageUri: String) = "photo_resize/$imageUri"
    }
}