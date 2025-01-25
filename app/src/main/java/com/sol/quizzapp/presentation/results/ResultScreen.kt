package com.sol.quizzapp.presentation.results

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sol.quizzapp.data.local.flag.FlagEntity
import com.sol.quizzapp.data.local.quiz.QuizEntity
import com.sol.quizzapp.data.local.wordle.WordleEntity
import com.sol.quizzapp.presentation.utils.AnimatedLazyColumn
import com.sol.quizzapp.presentation.utils.ExpandableCard
import java.text.DateFormat
import java.util.Date

@Composable
fun ResultScreen(viewModel: ResultViewModel = hiltViewModel()) {
    val quiz by viewModel.quizResult.collectAsState(initial = emptyList())
    val flag by viewModel.flagResult.collectAsState(initial = emptyList())
    val wordle by viewModel.wordleResult.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 32.dp, horizontal = 16.dp)
    ) {
        ExpandableCard("Quiz") {
            AnimatedLazyColumn(items = quiz) { QuizResultItem(it) }
        }
        ExpandableCard("Flag") {
            AnimatedLazyColumn(items = flag) { FlagResultItem(it) }
        }
        ExpandableCard("Wordle") {
            AnimatedLazyColumn(items = wordle) { WordleResultItem(it) }
        }
    }
}

@Composable
fun QuizResultItem(result: QuizEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(16.dp)
    ) {
        Text(text = "Categoría: ${result.category}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Dificultad: ${result.difficulty}", style = MaterialTheme.typography.bodySmall)
        Text(
            text = "Aciertos: ${result.correctAnswers}/${result.totalQuestions}",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "Fecha: ${DateFormat.getDateTimeInstance().format(Date(result.date))}",
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun FlagResultItem(result: FlagEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(16.dp)
    ) {
        Text(
            text = "Aciertos: ${result.correctAnswers}/${result.totalQuestions}",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "Fecha: ${DateFormat.getDateTimeInstance().format(Date(result.date))}",
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun WordleResultItem(result: WordleEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(16.dp)
    ) {
        Text(text = "Palabra: ${result.word}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Dificultad: ${result.difficulty}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Gano el juego: ${result.gameWon}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Intentos: ${result.attempts}", style = MaterialTheme.typography.bodySmall)
        Text(
            text = "Fecha: ${DateFormat.getDateTimeInstance().format(Date(result.date))}",
            style = MaterialTheme.typography.titleSmall
        )
    }
}