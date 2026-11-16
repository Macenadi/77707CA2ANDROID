package com.example.a77707_ca2_android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExceptionsScreen() {

    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Exceptions 1")

        Button(onClick = {

            val isLightOn = false

            try {
                check(isLightOn) { "Light is off" }
                result = "Light is on"
            } catch (e: IllegalStateException) {
                result = "IllegalStateException caught: ${e.message}"
            }

        }) {
            Text("Test Check()")
        }

        Button(onClick = {

            val number = -5

            try {
                require(number >= 0) { "Number cannot be negative" }
                result = "Number is valid"
            } catch (e: IllegalArgumentException) {
                result = "IllegalArgumentException caught: ${e.message}"
            }

        }) {
            Text("Test Require()")
        }

        Text(result)
    }
}