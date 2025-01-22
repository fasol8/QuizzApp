package com.sol.quizzapp.presentation.wordle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sol.quizzapp.navigation.QuizzesScreen

@Composable
fun WordleMenu(navController: NavController) {

    Column(Modifier
        .fillMaxSize()
        .padding(30.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(8.dp),
            onClick = { navController.navigate(QuizzesScreen.WordleScreen.route + "/random") },
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
        ) {
            Text("Random")
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(8.dp),
            onClick = { navController.navigate(QuizzesScreen.WordleScreen.route + "/easy") }) {
            Text("Easy")
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(8.dp),
            onClick = { navController.navigate(QuizzesScreen.WordleScreen.route + "/medium") }) {
            Text("Medium")
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(8.dp),
            onClick = { navController.navigate(QuizzesScreen.WordleScreen.route + "/hard") }) {
            Text("Hard")
        }
    }
}