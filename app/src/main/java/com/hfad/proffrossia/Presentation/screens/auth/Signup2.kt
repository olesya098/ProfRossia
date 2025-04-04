package com.hfad.proffrossia.Presentation.screens.auth

import android.content.Intent
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import android.net.Uri
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
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
fun Signup2(navController: NavController) {
    var checked by remember { mutableStateOf(false) }
    val textstyle = FontFamily(Font(R.font.fredokaregular))
    var password by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }
    var confirmpassword by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current


    fun isPasswordValid(password: String): Boolean {
        val passwordPattern =
            """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!"#$%&'()*+,-./:;<=>?@^_`{|}~])(?=\S+$).{8,}$""".toRegex()
        return passwordPattern.matches(password)
    }




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
                    fontFamily = textstyle,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = errorMessage,
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
                                navController.navigate(Screen.Signup.route)
                            }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(deepblue)
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier.background(if (isSystemInDarkTheme()) Darkteam else Color.White),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, start = 20.dp, end = 20.dp)
                        .height(56.dp)
                        .background(gradient, shape = RoundedCornerShape(15.dp))
                        .clickable {
                            when {
                                password.isBlank() -> {
                                    errorMessage = "Пожалуйста, введите пароль"
                                    showErrorDialog = true
                                }

                                confirmpassword.isBlank() -> {
                                    errorMessage = "Пожалуйста, введите пароль повторно"
                                    showErrorDialog = true
                                }

                                password != confirmpassword -> {
                                    errorMessage = "Пароли не совпадают"
                                    showErrorDialog = true
                                }

                                !isPasswordValid(password) -> {
                                    errorMessage = """
                                        Пароль должен содержать:
                                        - минимум 8 символов
                                        - заглавные и строчные буквы
                                        - хотя бы одну цифру
                                        - хотя бы один спецсимвол (@#$%^&+=!)
                                        - без пробелов
                                    """.trimIndent()
                                    showErrorDialog = true
                                }

                                !checked -> {
                                    errorMessage = "Пожалуйста, примите условия соглашения"
                                    showErrorDialog = true
                                }

                                else -> {
                                    if (NetworkUtils.isInternetAvailable(context)) {
                                        navController.navigate(Screen.Login.route)
                                    } else {
                                        navController.navigate(Screen.NoConnection.route)
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Signup",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(if (isSystemInDarkTheme()) Darkteam else Color.White)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Choose a Password",
                    fontSize = 22.sp,
                    fontFamily = textstyle,
                    color = if (isSystemInDarkTheme()) Color.White else greylabel,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                modifier = Modifier
                    .background(if (isSystemInDarkTheme()) Darkteam else Color.White)
                    .padding(start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Password",
                    color = if (isSystemInDarkTheme()) Color.White else greylabel,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontFamily = textstyle
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
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
                    label = { Text(text = "*******", fontFamily = textstyle) },
                    visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.eye),
                            contentDescription = null,
                            tint = greyMedium,
                            modifier = Modifier.clickable { visible = !visible }
                        )
                    },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Confirm Password",
                    color = if (isSystemInDarkTheme()) Color.White else greylabel,
                    fontFamily = textstyle,
                    modifier = Modifier.padding(bottom = 8.dp, top = 20.dp)
                )
                TextField(
                    value = confirmpassword,
                    onValueChange = { confirmpassword = it },
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
                    label = { Text(text = "*******", fontFamily = textstyle) },
                    visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.eye),
                            contentDescription = null,
                            tint = greyMedium,
                            modifier = Modifier.clickable { visible = !visible }
                        )
                    },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 40.dp)
                        .clickable { checked = !checked },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = null,
                        colors = CheckboxDefaults.colors(
                            checkedColor = blue,
                            uncheckedColor = blue,
                            checkmarkColor = Color.White
                        ),
                        modifier = Modifier.padding(end = 8.dp)

                    )
                    Row {

                        Text(
                            text = "I",
                            fontFamily = textstyle,
                            color = greyDark,
                            fontSize = 17.sp,
                            modifier = Modifier.fillMaxWidth(),
                            softWrap = true
                        )

                        Text(
                            fontFamily = textstyle,
                            fontSize = 17.sp,
                            color = blue,
                            modifier = Modifier.fillMaxWidth(),
                            softWrap = true,

                            text = "have made myself acquainted with the Rules"
                        )
                        Text(
                            text = "and accept all its provisions,",
                            fontFamily = textstyle,
                            color = greyDark,

                            fontSize = 17.sp,
                            modifier = Modifier.fillMaxWidth(),
                            softWrap = true
                        )
                    }

                }
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun Signup2Preview() {
    Signup2(rememberNavController())
}