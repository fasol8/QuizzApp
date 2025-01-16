package com.sol.quizzapp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sol.quizzapp.domain.model.quiz.TriviaCategory
import com.sol.quizzapp.navigation.QuizzesScreen
import java.util.Locale

@Composable
fun QuizMenu(navController: NavController, viewModel: QuizViewModel = hiltViewModel()) {

    var difficultSelected = remember { mutableStateOf("easy") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, start = 8.dp, end = 8.dp)
    ) {
        DifficultyBox(difficultSelected)
        Spacer(Modifier.height(8.dp))
        val categories = TriviaCategory.entries
        TriviaCategoryGrid(categories, navController, difficultSelected)
    }
}

@Composable
fun DifficultyBox(difficultSelected: MutableState<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val difficulties = listOf("easy", "medium", "hard")

        difficulties.forEach { difficulty ->
            val colorSelected = if (difficultSelected.value == difficulty)
                MaterialTheme.colorScheme.primary // Color resaltado
            else
                MaterialTheme.colorScheme.error // Color normal
            Button(
                onClick = { difficultSelected.value = difficulty },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorSelected,
                    contentColor = Color.White,
                    disabledContainerColor = colorSelected,
                    disabledContentColor = Color.White
                )
            ) {
                Text(difficulty.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
            }
        }
    }
}

@Composable
fun TriviaCategoryGrid(
    categories: List<TriviaCategory>,
    navController: NavController,
    difficultSelected: MutableState<String>
) {
    LazyVerticalGrid(
        GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(categories) { category ->
            TriviaCategoryItem(category) {
                navController.navigate(QuizzesScreen.QuizScreen.route + "/${category.id}-${difficultSelected.value}")
            }
        }
    }
}

@Composable
fun TriviaCategoryItem(category: TriviaCategory, onClick: () -> Unit) {
    Card { }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .aspectRatio(1f),
        onClick = { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = category.displayName,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
