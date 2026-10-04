package com.example.fittreino

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.fittreino.ui.FitTreinoMainApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mantém o suporte a tela cheia (Edge to Edge)
        enableEdgeToEdge()

        // Define a interface criada em Jetpack Compose
        setContent {
            FitTreinoMainApp()
        }
    }
}