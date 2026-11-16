package com.example.a77707_ca2_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

import com.google.firebase.auth.FirebaseAuth
import com.example.a77707_ca2_android.ui.theme._77707CA2ANDROIDTheme


class MainActivity : ComponentActivity() {

    // Firebase Authentication
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            // Question 2 - Firebase Login State
            var isLoggedIn by remember {
                mutableStateOf(auth.currentUser != null)
            }

            var email by remember {
                mutableStateOf("")
            }

            var password by remember {
                mutableStateOf("")
            }

            var errorMessage by remember {
                mutableStateOf("")
            }

            var isLoading by remember {
                mutableStateOf(false)
            }

            // Question 1 - Counters
            var counter1 by remember {
                mutableIntStateOf(0)
            }

            var counter2 by rememberSaveable {
                mutableIntStateOf(0)
            }

            // Navigation between screens
            var currentScreen by remember {
                mutableStateOf("Counters")
            }

            _77707CA2ANDROIDTheme {

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    if (isLoggedIn) {

                        when (currentScreen) {

                            "Exceptions1" -> {

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                ) {

                                    Button(
                                        onClick = {
                                            currentScreen = "Counters"
                                        },
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text("Back to Counters")
                                    }

                                    ExceptionsScreen()
                                }
                            }

                            "Exceptions2" -> {

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                ) {

                                    Button(
                                        onClick = {
                                            currentScreen = "Counters"
                                        },
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text("Back to Counters")
                                    }

                                    Exceptions2Screen()
                                }
                            }

                            else -> {

                                CounterScreen(
                                    counter1 = counter1,
                                    counter2 = counter2,

                                    onIncrement = {
                                        counter1++
                                        counter2++
                                    },

                                    onExceptionsClick = {
                                        currentScreen = "Exceptions1"
                                    },

                                    onExceptions2Click = {
                                        currentScreen = "Exceptions2"
                                    },

                                    onLogout = {
                                        auth.signOut()

                                        isLoggedIn = false
                                        currentScreen = "Counters"

                                        email = ""
                                        password = ""
                                        errorMessage = ""

                                        counter1 = 0
                                        counter2 = 0
                                    },

                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                        }

                    } else {

                        // Question 2 - Firebase Login
                        LoginScreen(
                            email = email,
                            password = password,
                            errorMessage = errorMessage,
                            isLoading = isLoading,

                            onEmailChange = {
                                email = it
                            },

                            onPasswordChange = {
                                password = it
                            },

                            onLogin = {

                                if (email.isBlank() || password.isBlank()) {

                                    errorMessage =
                                        "Please enter your email and password."

                                } else {

                                    isLoading = true
                                    errorMessage = ""

                                    auth.signInWithEmailAndPassword(
                                        email.trim(),
                                        password
                                    ).addOnCompleteListener(this@MainActivity) { task ->

                                        isLoading = false

                                        if (task.isSuccessful) {

                                            isLoggedIn = true
                                            errorMessage = ""
                                            password = ""

                                        } else {

                                            errorMessage =
                                                "Login failed. Please check your credentials."
                                        }
                                    }
                                }
                            },

                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}


// QUESTION 2 - LOGIN SCREEN

@Composable
fun LoginScreen(
    email: String,
    password: String,
    errorMessage: String,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Firebase Login")

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = {
                Text("Email")
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = {
                Text("Password")
            },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onLogin,
            enabled = !isLoading
        ) {
            Text("Login")
        }

        if (isLoading) {

            Spacer(modifier = Modifier.height(12.dp))

            CircularProgressIndicator()
        }

        if (errorMessage.isNotEmpty()) {

            Spacer(modifier = Modifier.height(12.dp))

            Text(errorMessage)
        }
    }
}


// QUESTION 1 - COUNTER SCREEN

@Composable
fun CounterScreen(
    counter1: Int,
    counter2: Int,
    onIncrement: () -> Unit,
    onExceptionsClick: () -> Unit,
    onExceptions2Click: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Counter 1 (Remember): $counter1"
        )

        Text(
            text = "Counter 2 (RememberSaveable): $counter2"
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onIncrement
        ) {
            Text("Increment")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Question 3 - Exceptions 1
        Button(
            onClick = onExceptionsClick
        ) {
            Text("Exceptions 1")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Question 4 - Exceptions 2
        Button(
            onClick = onExceptions2Click
        ) {
            Text("Exceptions 2")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onLogout
        ) {
            Text("Logout")
        }
    }
}


// PREVIEW

@Preview(showBackground = true)
@Composable
fun CounterScreenPreview() {

    _77707CA2ANDROIDTheme {

        CounterScreen(
            counter1 = 0,
            counter2 = 0,
            onIncrement = {},
            onExceptionsClick = {},
            onExceptions2Click = {},
            onLogout = {}
        )
    }
}