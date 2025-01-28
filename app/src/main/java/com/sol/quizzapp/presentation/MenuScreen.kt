package com.sol.quizzapp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sol.quizzapp.R
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
                    .height(200.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                onClick = { navController.navigate(QuizzesScreen.QuizMenuScreen.route) }) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "QUIZ",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                    Icon(
                        painter = painterResource(R.drawable.quiz),
                        contentDescription = "game quiz",
                        modifier = Modifier
                            .size(180.dp)
                            .padding(12.dp)
                    )

                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 16.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                onClick = { navController.navigate(QuizzesScreen.FlagScreen.route) }) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "FLAG",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                    Icon(
                        painter = painterResource(R.drawable.flag),
                        contentDescription = "game flag",
                        modifier = Modifier
                            .size(180.dp)
                            .padding(12.dp)
                    )
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 16.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                onClick = { navController.navigate(QuizzesScreen.WordleMenuScreen.route) }) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "WORDLE",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                    Icon(
                        painter = painterResource(R.drawable.wordle),
                        contentDescription = "game wordle",
                        modifier = Modifier
                            .size(180.dp)
                            .padding(12.dp)
                    )
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 16.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                onClick = { navController.navigate(QuizzesScreen.LogoScreen.route) }) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "LOGO",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                    Icon(
                        painter = painterResource(R.drawable.difficult_default),
                        contentDescription = "game logo",
                        modifier = Modifier
                            .size(180.dp)
                            .padding(12.dp)
                    )
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 16.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                onClick = { navController.navigate(QuizzesScreen.ResultScreen.route) }) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "STATS",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                    Icon(
                        painter = painterResource(R.drawable.stats),
                        contentDescription = "stats",
                        modifier = Modifier
                            .size(180.dp)
                            .padding(12.dp)
                    )

                }
            }
        }
    }
}