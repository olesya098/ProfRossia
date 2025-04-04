package com.hfad.proffrossia.Presentation.screens.auth

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
import com.hfad.proffrossia.ui.theme.red
import com.hfad.proffrossia.utils.NetworkUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }
    var errorPass by remember { mutableStateOf(false) }
    var errorEmail by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    val textstule = FontFamily(Font(R.font.fredokaregular))
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
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = {
                Text(
                    text = "Ошибка",
                    fontFamily = textstule,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Пожалуйста, заполните все поля",
                    fontFamily = textstule
                )
            },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = blue)
                ) {
                    Text(
                        text = "OK",
                        fontFamily = textstule,
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
                            text = "Login",
                            color = Color.White,
                            fontFamily = textstule,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    deepblue
                ),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                navController.navigate(Screen.LanguageSelect.route)
                            }
                    )
                }
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
                        .padding(
                            top = 30.dp, start = 20.dp, end = 20.dp
                        )
                        .height(56.dp)
                        .background(
                            gradient,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .clickable {
                            if (email.isBlank() || password.isBlank()) {
                                showErrorDialog = true
                            } else {
                                if (NetworkUtils.isInternetAvailable(context)) {
                                    navController.navigate(Screen.MainScreen.route)
                                } else {
                                    navController.navigate(Screen.NoConnection.route)
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Login",
                        color = Color.White,
                        fontFamily = textstule,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 60.dp),
                ) {
                    Text(
                        text = "Not you member? ",
                        fontFamily = textstule,
                        color = greyDark,
                        fontSize = 17.sp

                    )
                    Text(
                        text = "Signup",
                        modifier = Modifier
                            .clickable {
                                navController.navigate(Screen.Signup.route)
                            },
                        fontFamily = textstule,
                        color = blue,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center

                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(if (isSystemInDarkTheme()) Darkteam else Color.White)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .background(if (isSystemInDarkTheme()) Darkteam else Color.White),

                ) {

                Image(
                    painter = painterResource(id = R.drawable.login),
                    contentDescription = null,
                    modifier = Modifier.size(90.dp)
                )
                Text(
                    text = "For free, join now and \n start learning",
                    fontFamily = textstule,
                    lineHeight = 30.sp,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    color = if (isSystemInDarkTheme()) Color.White else greylabel,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 30.dp, end = 30.dp)
                )
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceEvenly,

                    ) {

                    Text(
                        text = "Email Address",
                        fontFamily = textstule,
                        modifier = Modifier.padding(
                            top = 20.dp,
                            bottom = 8.dp
                        ),
                        fontSize = 15.sp,
                        color = if (isSystemInDarkTheme()) Color.White else greylabel

                    )

                    TextField(
                        value = email,
                        onValueChange = {
                            email = it
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
                                fontFamily = textstule,
                            )
                        },
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()

                    )



                    Text(
                        text = "Password",
                        fontFamily = textstule,
                        modifier = Modifier.padding(
                            top = 20.dp,
                            bottom = 8.dp
                        ),
                        fontSize = 15.sp,
                        color = if (isSystemInDarkTheme()) Color.White else greylabel

                    )
                    TextField(
                        value = password,
                        onValueChange = {
                            password = it
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
                                text = "*******",
                                fontWeight = FontWeight.Bold
                            )
                        },
                        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.eye),
                                contentDescription = null,
                                modifier = Modifier
                                    .clickable {
                                        visible = !visible
                                    }
                            )
                        },
                        shape = RoundedCornerShape(15.dp),

                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Text(
                        text = "Forgot Password ",
                        modifier = Modifier.padding(
                            top = 8.dp,
                            bottom = 20.dp
                        ),
                        fontFamily = textstule,
                        fontSize = 15.sp,
                        color = red
                    )

                }


            }

        }

        if (errorEmail) {
            Dialog(
                onDismissRequest = { }
            ) {
                Text(
                    text = "Ошибка в Email"
                )

            }
        }
        if (errorPass) {
            Dialog(
                onDismissRequest = { }
            ) {
                Text(
                    text = "Ошибка в пароле"
                )

            }
        }

    }
}

@PreviewScreenSizes
@Composable
fun LoginPreview() {
    Login(rememberNavController())
}

