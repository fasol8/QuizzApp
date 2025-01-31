package com.sol.quizzapp.presentation.flag

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sol.quizzapp.R
import com.sol.quizzapp.presentation.utils.QuizQuestion
import com.sol.quizzapp.presentation.utils.ResultScreen
import com.sol.quizzapp.presentation.utils.playSound
import com.sol.quizzapp.presentation.utils.vibrate

@Composable
fun FlagScreen(navController: NavController, viewModel: FlagViewModel = hiltViewModel()) {
    val question by viewModel.currentQuestion.collectAsState()
    val currentRound by viewModel.currentRound.collectAsState()
    val score by viewModel.score.collectAsState()
    val quizFinished by viewModel.quizFinished.collectAsState()
    val remainingTime by viewModel.remainingTime.collectAsState()
    val selectedAnswer by viewModel.selectedAnswer.collectAsState(null)
    val nextButtonEnabled by viewModel.nextButtonEnabled.collectAsState()
    val timeExpired by viewModel.timeExpired.collectAsState(false)
    val context = LocalContext.current

    LaunchedEffect(remainingTime, selectedAnswer) {
        if (remainingTime > 0 && selectedAnswer == null) {
            kotlinx.coroutines.delay(1000)
            viewModel.decrementTimer()
        }
    }

    LaunchedEffect(selectedAnswer) {
        selectedAnswer?.let { answer ->
            if (question!!.correctCountry == answer) {
                playSound(context, R.raw.correct)
            } else {
                playSound(context, R.raw.wrong)
                vibrate(context)
            }
        }
    }

    LaunchedEffect(timeExpired) {
        if (timeExpired) {
            playSound(context, R.raw.timer_end)
            vibrate(context, 500)
        }
    }

    if (quizFinished) {
        FlagQuizResult(
            score = score,
            totalRounds = 10,
            navController = navController,
            viewModel = viewModel
        )
    } else if (question != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(Modifier.heightIn(64.dp))
                QuizQuestion(
                    questionText = "Which country does this flag belong to?",
                    imageUrl = "https://flagsapi.com/${question!!.correctCountry.code}/flat/64.png",
                    options = question!!.options,
                    correctAnswer = question!!.correctCountry,
                    selectedAnswer = selectedAnswer,
                    onAnswerSelected = { viewModel.onAnswerSelected(it) },
                    timeExpired = timeExpired,
                    currentRound = currentRound,
                    remainingTime = remainingTime,
                )
            }
            val buttonScale by animateFloatAsState(targetValue = if (nextButtonEnabled) 1.1f else 1f)
            Button(
                onClick = {
                    playSound(context, R.raw.clic)
                    viewModel.onNextClicked()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .scale(buttonScale),
                enabled = nextButtonEnabled
            ) {
                Text(text = "Next")
            }
        }
    }
}

@Composable
fun FlagQuizResult(
    score: Int,
    totalRounds: Int,
    navController: NavController,
    viewModel: FlagViewModel
) {
    ResultScreen(
        navController = navController,
        title = "¡Flag Quiz Finish!",
        message = "¡Congratulations on completing the flag quiz!",
        score = score,
        totalRounds = totalRounds,
        onSaveResult = { viewModel.saveResult(score, totalRounds) }
    )
}