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

// Firebase Authentication
import com.google.firebase.auth.FirebaseAuth

// Application Theme
import com.example.a77707_ca2_android.ui.theme._77707CA2ANDROIDTheme


class MainActivity : ComponentActivity() {

    // Firebase Authentication instance
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            // Check if the user is already logged in
            var isLoggedIn by remember {
                mutableStateOf(auth.currentUser != null)
            }

            // Email and password fields
            var email by remember {
                mutableStateOf("")
            }

            var password by remember {
                mutableStateOf("")
            }

            // Authentication state
            var errorMessage by remember {
                mutableStateOf("")
            }

            var isLoading by remember {
                mutableStateOf(false)
            }

            // Counter 1 uses remember
            // It resets when the Activity is recreated
            var counter1 by remember {
                mutableIntStateOf(0)
            }

            // Counter 2 uses rememberSaveable
            // It keeps its value after configuration changes
            var counter2 by rememberSaveable {
                mutableIntStateOf(0)
            }

            _77707CA2ANDROIDTheme {

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    if (isLoggedIn) {

                        // Display counters after successful login
                        CounterScreen(
                            counter1 = counter1,
                            counter2 = counter2,

                            onIncrement = {
                                counter1++
                                counter2++
                            },

                            onLogout = {
                                auth.signOut()

                                isLoggedIn = false
                                email = ""
                                password = ""
                                errorMessage = ""

                                counter1 = 0
                                counter2 = 0
                            },

                            modifier = Modifier.padding(innerPadding)
                        )

                    } else {

                        // Display Firebase login screen
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

                                    // Firebase Email/Password Authentication
                                    auth.signInWithEmailAndPassword(
                                        email.trim(),
                                        password
                                    ).addOnCompleteListener(this@MainActivity) { task ->

                                        isLoading = false

                                        if (task.isSuccessful) {

                                            // Login successful
                                            isLoggedIn = true
                                            errorMessage = ""
                                            password = ""

                                        } else {

                                            // Login failed
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


// LOGIN SCREEN
// Receives state and callbacks from MainActivity

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

        Text(
            text = "Firebase Login"
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // Email field
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = {
                Text("Email")
            },
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = {
                Text("Password")
            },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // Login button
        Button(
            onClick = onLogin,
            enabled = !isLoading
        ) {
            Text("Login")
        }

        // Loading indicator
        if (isLoading) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            CircularProgressIndicator()
        }

        // Error message
        if (errorMessage.isNotEmpty()) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = errorMessage
            )
        }
    }
}


// COUNTER SCREEN
// State Hoisting:
// Receives counter values and actions from MainActivity

@Composable
fun CounterScreen(
    counter1: Int,
    counter2: Int,
    onIncrement: () -> Unit,
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

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = onIncrement
        ) {
            Text("Increment")
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // Logout button
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
            onLogout = {}
        )
    }
}