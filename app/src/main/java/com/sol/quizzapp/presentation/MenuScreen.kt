package com.sol.quizzapp.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sol.quizzapp.navigation.QuizzesScreen

@Composable
fun MenuScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Spacer(Modifier.height(32.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                onClick = { navController.navigate(QuizzesScreen.QuizMenuScreen.route) }) {
                Text("QUIZ")
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(vertical = 16.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                onClick = { navController.navigate(QuizzesScreen.FlagScreen.route) }) {
                Text("FLAG")
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(vertical = 16.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                onClick = { navController.navigate(QuizzesScreen.WordleMenuScreen.route) }) {
                Text("WORDLE")
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(vertical = 16.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                onClick = { navController.navigate(QuizzesScreen.ResultScreen.route) }) {
                Text("RESULT")
            }
        }
    }
}