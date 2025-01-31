package com.sol.quizzapp.presentation.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.sol.quizzapp.R
import com.sol.quizzapp.domain.model.quiz.TriviaCategory
import com.sol.quizzapp.domain.model.util.DifficultMode
import com.sol.quizzapp.presentation.utils.QuizQuestion
import com.sol.quizzapp.presentation.utils.ResultScreen
import com.sol.quizzapp.presentation.utils.decodeHtmlEntities
import com.sol.quizzapp.presentation.utils.playSound
import com.sol.quizzapp.presentation.utils.vibrate

@Composable
fun QuizScreen(
    navController: NavHostController,
    categoryId: Int,
    difficultSelected: DifficultMode,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val quiz by viewModel.quiz.observeAsState(emptyList())
    val currentQuestionIndex by viewModel.currentQuestionIndex.observeAsState(0)
    val selectedAnswer by viewModel.selectedAnswer.observeAsState(null)
    val isNextEnabled by viewModel.isNextEnabled.observeAsState(false)
    val timerValue by viewModel.timerValue.observeAsState(30)
    val isQuizFinished by viewModel.isQuizFinished.observeAsState(false)
    val score by viewModel.score.observeAsState(0)
    val timeExpired by viewModel.timeExpired.observeAsState(false)
    val shuffledOptions by viewModel.shuffledOptions.observeAsState(emptyList())
    val context = LocalContext.current

    LaunchedEffect(categoryId, difficultSelected) {
        viewModel.getLoad(categoryId, difficultSelected.value)
    }

    LaunchedEffect(key1 = timerValue) {
        if (timerValue > 0) {
            kotlinx.coroutines.delay(1000)
            viewModel.decrementTimer()
        }
    }

    LaunchedEffect(selectedAnswer) {
        selectedAnswer?.let { answer ->
            if (quiz[currentQuestionIndex].correctAnswer == answer) {
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

    AnimatedVisibility(visible = !isQuizFinished) {
        if (quiz.isNotEmpty()) {
            val quizItem = quiz[currentQuestionIndex]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    Spacer(Modifier.heightIn(64.dp))
                    QuizQuestion(
                        questionText = decodeHtmlEntities(quizItem.question),
                        imageUrl = null,
                        options = shuffledOptions ?: emptyList(),
                        correctAnswer = quizItem.correctAnswer,
                        selectedAnswer = selectedAnswer,
                        onAnswerSelected = { viewModel.onAnswerSelected(it) },
                        timeExpired = timeExpired,
                        remainingTime = timerValue,
                        currentRound = currentQuestionIndex,
                    )
                }
                AnimatedVisibility(
                    visible = isNextEnabled,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Button(
                        onClick = {
                            playSound(context, R.raw.clic)
                            viewModel.onNextQuestion()
                        },
                        enabled = isNextEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    ) {
                        Text("Next")
                    }
                }
            }
        }
    }

    AnimatedVisibility(visible = isQuizFinished) {
        QuizResultScreen(
            score = score,
            totalQuestions = quiz.size,
            navController = navController,
            viewModel = viewModel,
            categoryId = categoryId,
            difficultSelected = difficultSelected.value
        )
    }
}

@Composable
fun QuizResultScreen(
    score: Int,
    totalQuestions: Int,
    navController: NavController,
    viewModel: QuizViewModel,
    categoryId: Int,
    difficultSelected: String
) {
    val category = TriviaCategory.fromId(categoryId)?.displayName ?: "Trivia"

    ResultScreen(
        navController = navController,
        title = "¡Trivia Finish!",
        message = "Category: $category\nDifficult $difficultSelected",
        score = score,
        totalRounds = totalQuestions,
        onSaveResult = {
            viewModel.saveResult(category, difficultSelected, score, totalQuestions)
        }
    )
}