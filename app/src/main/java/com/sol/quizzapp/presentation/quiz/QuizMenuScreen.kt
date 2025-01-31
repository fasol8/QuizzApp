package com.sol.quizzapp.presentation.quiz

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sol.quizzapp.domain.model.quiz.TriviaCategory
import com.sol.quizzapp.domain.model.util.DifficultMode
import com.sol.quizzapp.navigation.QuizzesScreen
import com.sol.quizzapp.presentation.utils.DifficultyBox

@Composable
fun QuizMenu(navController: NavController) {

    val difficultSelected = remember { mutableStateOf(DifficultMode.EASY) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, start = 8.dp, end = 8.dp)
    ) {
        DifficultyBox(difficultSelected)
        Spacer(Modifier.height(4.dp))
        Button(
            onClick = { navController.navigate(QuizzesScreen.QuizScreen.route + "/${0}-${difficultSelected.value.value}") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Random Quiz")
        }
        Spacer(Modifier.height(4.dp))
        val categories = TriviaCategory.entries
        TriviaCategoryGrid(categories, navController, difficultSelected)
    }
}

@Composable
fun TriviaCategoryGrid(
    categories: List<TriviaCategory>,
    navController: NavController,
    difficultSelected: MutableState<DifficultMode>
) {
    LazyVerticalGrid(
        GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(categories.drop(1)) { category ->
            TriviaCategoryItem(category) {
                navController.navigate(QuizzesScreen.QuizScreen.route + "/${category.id}-${difficultSelected.value}")
            }
        }
    }
}

@Composable
fun TriviaCategoryItem(category: TriviaCategory, onClick: () -> Unit) {
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
            Column {
                Icon(
                    painter = painterResource(category.icon),
                    contentDescription = "icon category",
                    modifier = Modifier.size(100.dp)
                )
                Text(
                    text = category.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
