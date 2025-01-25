package com.sol.quizzapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.sol.quizzapp.navigation.QuizNavHost
import com.sol.quizzapp.ui.theme.QuizzAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizzAppTheme (dynamicColor = false){
                val navController = rememberNavController()
                QuizNavHost(navController)
            }
        }
    }
}
