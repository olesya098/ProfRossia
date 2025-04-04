package com.hfad.proffrossia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hfad.proffrossia.Presentation.navigation.Navigatia
import com.hfad.proffrossia.ui.theme.ProffRossiaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            ProffRossiaTheme {
                Navigatia()



            }
        }
    }
}