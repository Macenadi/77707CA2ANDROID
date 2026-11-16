package com.example.a77707_ca2_android

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Exceptions2Screen() {

    var word by remember { mutableStateOf("") }
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

            try {
                if (word.length > 10) {
                    throw CustomException()
                }

                result = "Valid word: $word"

            } catch (e: CustomException) {
                result = e.toString()
            }

        }) {
            Text("Check Word")
        }

        Text(result)
    }
}