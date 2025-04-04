package com.hfad.proffrossia.Presentation.screens.auth

import android.provider.ContactsContract.CommonDataKinds.Email
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hfad.proffrossia.R
import com.hfad.proffrossia.Presentation.navigation.Screen
import com.hfad.proffrossia.ui.theme.Darkteam
import com.hfad.proffrossia.ui.theme.blue
import com.hfad.proffrossia.ui.theme.deepblue
import com.hfad.proffrossia.ui.theme.greyDark
import com.hfad.proffrossia.ui.theme.greyLite
import com.hfad.proffrossia.ui.theme.greyMedium
import com.hfad.proffrossia.ui.theme.greylabel
import com.hfad.proffrossia.utils.NetworkUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Signup(navController: NavController) {
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var emailadress by remember { mutableStateOf("") }
    val textstyle = FontFamily(Font(R.font.fredokaregular))
    var errorMessage by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current


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

    fun isEmailValid(email: String): Boolean {
        val emailPattern = "^[a-z0-9]+@[a-z0-9]+\\.[a-z]{2,}\$".toRegex()
        return emailPattern.matches(email)
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = {
                Text(
                    text = "Ошибка",
                    fontFamily = textstyle,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Неверная почта",
                    fontFamily = textstyle
                )
            },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = blue)
                ) {
                    Text(
                        text = "OK",
                        fontFamily = textstyle,
                        color = Color.White
                    )
                }
            }
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Signup",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.White,
                            fontFamily = textstyle

                        )
                    }
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .clickable {
                                navController.navigate(Screen.Login.route)
                            }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    deepblue
                )
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(if (isSystemInDarkTheme()) Darkteam else Color.White),

                ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, start = 20.dp, end = 20.dp)
                        .height(56.dp)
                        .background(gradient, shape = RoundedCornerShape(15.dp))
                        .clickable {
                            when {
                                firstname.isBlank() -> {
                                    errorMessage = "Пожалуйста, введите имя"
                                    showErrorDialog = true
                                }

                                lastname.isBlank() -> {
                                    errorMessage = "Пожалуйста, введите фамилию"
                                    showErrorDialog = true
                                }

                                emailadress.isBlank() -> {
                                    errorMessage = "Пожалуйста, введите email"
                                    showErrorDialog = true
                                }

                                !isEmailValid(emailadress) -> {
                                    errorMessage = "Некорректный email. Пример: name@domain.ru"
                                    showErrorDialog = true
                                }

                                else -> {
                                    if (NetworkUtils.isInternetAvailable(context)) {
                                        navController.navigate(Screen.Signup2.route)
                                    } else {
                                        navController.navigate(Screen.NoConnection.route)
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Continue",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = textstyle,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (isSystemInDarkTheme()) Darkteam else Color.White)
                        .padding(top = 20.dp, bottom = 60.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Already you member? ",
                        color = greyDark,
                        fontFamily = textstyle,
                        fontSize = 17.sp
                    )
                    Text(
                        text = "Login",
                        fontFamily = textstyle,
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.Login.route)
                        },
                        color = blue,
                        fontSize = 17.sp

                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isSystemInDarkTheme()) Darkteam else Color.White),

            ) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(if (isSystemInDarkTheme()) Darkteam else Color.White)
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (isSystemInDarkTheme()) Darkteam else Color.White)
                        .padding(top = 40.dp, bottom = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Create an Account",
                        fontSize = 22.sp,
                        fontFamily = textstyle,
                        color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    modifier = Modifier
                        .background(if (isSystemInDarkTheme()) Darkteam else Color.White)
                        .padding(
                            start = 20.dp,
                            end = 20.dp
                        )
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "First Name",
                        modifier = Modifier.padding(bottom = 8.dp),
                        fontFamily = textstyle,
                        color = if (isSystemInDarkTheme()) Color.White else greylabel
                    )
                    TextField(
                        value = firstname,
                        onValueChange = {
                            firstname = it
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = if (isSystemInDarkTheme()) greyDark else greyLite,
                            unfocusedContainerColor = if (isSystemInDarkTheme()) greyDark else greyLite,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            focusedLabelColor = if (isSystemInDarkTheme()) Color.LightGray else greyMedium,
                            unfocusedLabelColor = if (isSystemInDarkTheme()) Color.LightGray else greyMedium,
                            cursorColor = if (isSystemInDarkTheme()) Color.White else Color.Black
                        ),
                        label = {
                            Text(
                                text = "Your First Name",
                                fontFamily = textstyle,
                            )
                        },
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Text(
                        text = "Last Name",
                        modifier = Modifier.padding(
                            bottom = 8.dp,
                            top = 20.dp
                        ),
                        fontFamily = textstyle,
                        color = if (isSystemInDarkTheme()) Color.White else greylabel

                    )
                    TextField(
                        value = lastname,
                        onValueChange = {
                            lastname = it
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = if (isSystemInDarkTheme()) greyDark else greyLite,
                            unfocusedContainerColor = if (isSystemInDarkTheme()) greyDark else greyLite,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            focusedLabelColor = if (isSystemInDarkTheme()) Color.LightGray else greyMedium,
                            unfocusedLabelColor = if (isSystemInDarkTheme()) Color.LightGray else greyMedium,
                            cursorColor = if (isSystemInDarkTheme()) Color.White else Color.Black
                        ),
                        label = {
                            Text(
                                text = "Your Last Name",
                                fontFamily = textstyle,
                            )
                        },
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Text(
                        text = "Email Address",
                        modifier = Modifier.padding(
                            bottom = 8.dp,
                            top = 20.dp
                        ),
                        fontFamily = textstyle,
                        color = if (isSystemInDarkTheme()) Color.White else greylabel

                    )
                    TextField(
                        value = emailadress,
                        onValueChange = {
                            emailadress = it
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = if (isSystemInDarkTheme()) greyDark else greyLite,
                            unfocusedContainerColor = if (isSystemInDarkTheme()) greyDark else greyLite,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            focusedLabelColor = if (isSystemInDarkTheme()) Color.LightGray else greyMedium,
                            unfocusedLabelColor = if (isSystemInDarkTheme()) Color.LightGray else greyMedium,
                            cursorColor = if (isSystemInDarkTheme()) Color.White else Color.Black
                        ),
                        label = {
                            Text(
                                text = "Email",
                                fontFamily = textstyle,
                            )
                        },
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                }
            }

        }
    }
}

@PreviewScreenSizes
@Composable
fun SignupPreview() {
    Signup(rememberNavController())
}