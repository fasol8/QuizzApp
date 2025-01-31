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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sol.quizzapp.data.local.flag.FlagEntity
import com.sol.quizzapp.data.local.logo.LogoEntity
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
    val logo by viewModel.logoResult.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 32.dp, horizontal = 16.dp)
    ) {
        Text(
            text = "Stats",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )
        ExpandableCard("Quiz") {
            AnimatedLazyColumn(items = quiz) { QuizResultItem(it) }
        }
        ExpandableCard("Flag") {
            AnimatedLazyColumn(items = flag) { FlagResultItem(it) }
        }
        ExpandableCard("Wordle") {
            AnimatedLazyColumn(items = wordle) { WordleResultItem(it) }
        }
        ExpandableCard("Logo") {
            AnimatedLazyColumn(items = logo) { LogoResultItem(it) }
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
        Text(text = "Category: ${result.category}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Difficult: ${result.difficulty}", style = MaterialTheme.typography.bodySmall)
        Text(
            text = "Hits: ${result.correctAnswers}/${result.totalQuestions}",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "Date: ${DateFormat.getDateTimeInstance().format(Date(result.date))}",
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
            text = "Hits: ${result.correctAnswers}/${result.totalQuestions}",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "Date: ${DateFormat.getDateTimeInstance().format(Date(result.date))}",
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
        Text(text = "Word: ${result.word}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Difficult: ${result.difficulty}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Won the game: ${result.gameWon}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Attemps: ${result.attempts}", style = MaterialTheme.typography.bodySmall)
        Text(
            text = "Date: ${DateFormat.getDateTimeInstance().format(Date(result.date))}",
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun LogoResultItem(result: LogoEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(16.dp)
    ) {
        Text(
            text = "Hits: ${result.correctAnswers}/${result.totalQuestions}",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "Date: ${DateFormat.getDateTimeInstance().format(Date(result.date))}",
            style = MaterialTheme.typography.titleSmall
        )
    }
}