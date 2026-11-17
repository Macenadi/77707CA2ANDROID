package com.example.a77707_ca2_android

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.util.Log

@Composable
fun Exceptions2Screen() {

    // Store the word entered by the user
    var word by remember { mutableStateOf("") }

    // Store the result displayed on the screen
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Exceptions 2")

        OutlinedTextField(
            value = word,
            onValueChange = { word = it },
            label = { Text("Enter a word") },
            singleLine = true
        )

        Button(onClick = {

            //  Logcat page
            Log.d("ExceptionTest", "Checking word: $word")

            try {

                // Check if the word contains more than 10 characters
                if (word.length > 10) {

                    // Throw our custom exception
                    throw CustomException()
                }

                // If no exception occurs, the word is valid
                result = "Valid word: $word"

                // Print successful validation in Logcat
                Log.d("ExceptionTest", "Success: $result")

            } catch (e: CustomException) {

                // Catch the custom exception
                result = e.toString()

                // Print the exception message in Logcat
                Log.e("ExceptionTest", "CustomException caught: $result")
            }

        }) {
            Text("Check Word")
        }

        // Display the result on the screen
        Text(result)
    }
}