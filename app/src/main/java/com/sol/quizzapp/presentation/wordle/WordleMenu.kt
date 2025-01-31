package com.sol.quizzapp.presentation.wordle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
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
import com.sol.quizzapp.domain.model.util.DifficultMode
import com.sol.quizzapp.navigation.QuizzesScreen

@Composable
fun WordleMenu(navController: NavController) {

    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 28.dp, end = 8.dp, start = 8.dp)
    ) {
        Text(
            "WORDLE",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 148.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(8.dp),
            onClick = { navController.navigate(QuizzesScreen.WordleScreen.route + "/random") }
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.random),
                    contentDescription = "difficulty random",
                    modifier = Modifier
                        .size(170.dp)
                        .padding(12.dp)
                )
                Spacer(Modifier.width(48.dp))
                Text("Random", style = MaterialTheme.typography.titleLarge)
            }
        }

        DifficultMode.entries.forEach { difficulty ->
            val iconRes = when (difficulty) {
                DifficultMode.EASY -> R.drawable.easy
                DifficultMode.MEDIUM -> R.drawable.medium
                DifficultMode.HARD -> R.drawable.hard
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(8.dp),
                onClick = { navController.navigate(QuizzesScreen.WordleScreen.route + "/${difficulty.name.lowercase()}") }
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = "difficulty ${difficulty.name.lowercase()}",
                        modifier = Modifier
                            .size(170.dp)
                            .padding(12.dp)
                    )
                    Spacer(Modifier.width(48.dp))
                    Text(
                        difficulty.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}