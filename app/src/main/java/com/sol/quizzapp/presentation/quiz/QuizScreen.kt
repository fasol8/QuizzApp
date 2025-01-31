package com.sol.quizzapp.presentation.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sol.quizzapp.R
import com.sol.quizzapp.domain.model.quiz.QuizResult
import com.sol.quizzapp.domain.model.quiz.TriviaCategory
import com.sol.quizzapp.navigation.QuizzesScreen
import com.sol.quizzapp.presentation.utils.CircularTimer
import com.sol.quizzapp.presentation.utils.decodeHtmlEntities
import com.sol.quizzapp.presentation.utils.playSound
import com.sol.quizzapp.presentation.utils.vibrate
import com.sol.quizzapp.ui.theme.correct

@Composable
fun QuizScreen(
    navController: NavHostController,
    categoryId: Int,
    difficultSelected: String,
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
    val context = LocalContext.current

    LaunchedEffect(categoryId, difficultSelected) {
        viewModel.getLoad(categoryId, difficultSelected)
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
            val currentQuizItem = quiz[currentQuestionIndex]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    Spacer(Modifier.heightIn(64.dp))
                    CircularTimer(
                        timerValue,
                        currentQuestionIndex,
                        Modifier
                            .size(96.dp)
                            .align(Alignment.CenterHorizontally),
                        color1 = Color.Green,
                        color2 = Color.Red,
                        onTimerEnd = { println("¡El tiempo terminó!") }
                    )
                    ItemQuiz(
                        quizItem = currentQuizItem,
                        selectedAnswer = selectedAnswer,
                        onAnswerSelected = { viewModel.onAnswerSelected(it) },
                        timeExpired = timeExpired
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
        ResultQuizScreen(
            score = score,
            totalQuestions = quiz.size,
            navController,
            viewModel,
            categoryId,
            difficultSelected
        )
    }
}

@Composable
fun ItemQuiz(
    quizItem: QuizResult,
    selectedAnswer: String?,
    onAnswerSelected: (String) -> Unit,
    timeExpired: Boolean
) {
    val shuffledAnswers = remember(quizItem) {
        (quizItem.incorrectAnswers + quizItem.correctAnswer).shuffled()
    }

    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(quizItem.category, style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(4.dp))
        Text(decodeHtmlEntities(quizItem.question), style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        shuffledAnswers.forEach { answer ->
            val isCorrect = answer == quizItem.correctAnswer
            val backgroundColor = when {
                selectedAnswer == null -> MaterialTheme.colorScheme.primary
                selectedAnswer == answer && isCorrect -> correct
                selectedAnswer == answer && !isCorrect -> MaterialTheme.colorScheme.error
                isCorrect -> correct.copy(alpha = 0.5f)
                else -> MaterialTheme.colorScheme.primary
            }

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    if (selectedAnswer == null) {
                        onAnswerSelected(answer)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = backgroundColor,
                    contentColor = Color.White,
                    disabledContainerColor = backgroundColor,
                    disabledContentColor = Color.White
                ),
                enabled = !timeExpired && selectedAnswer == null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(decodeHtmlEntities(answer))
            }

            if (timeExpired) {
                Text(
                    "¡Tiempo agotado!",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun ResultQuizScreen(
    score: Int,
    totalQuestions: Int,
    navController: NavHostController,
    viewModel: QuizViewModel,
    categoryId: Int,
    difficultSelected: String
) {
    LaunchedEffect(Unit) {
        viewModel.saveResult(
            TriviaCategory.fromId(categoryId)!!.displayName,
            difficultSelected,
            score,
            totalQuestions
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¡Trivia finalizada!",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Aciertos: $score de $totalQuestions",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = { navController.navigate(QuizzesScreen.MenuScreen.route) }) {
            Text("Volver al menu")
        }
    }
}