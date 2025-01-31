package com.sol.quizzapp.presentation.flag

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.sol.quizzapp.R
import com.sol.quizzapp.domain.model.flags.Countries
import com.sol.quizzapp.domain.model.flags.QuestionCountry
import com.sol.quizzapp.navigation.QuizzesScreen
import com.sol.quizzapp.presentation.utils.CircularTimer
import com.sol.quizzapp.presentation.utils.playSound
import com.sol.quizzapp.presentation.utils.vibrate
import com.sol.quizzapp.ui.theme.correct

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
        QuizResult(score = score, totalRounds = 10, navController, viewModel)
    } else if (question != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(Modifier.heightIn(64.dp))
                CircularTimer(
                    remainingTime,
                    currentRound,
                    Modifier
                        .size(96.dp)
                        .align(Alignment.CenterHorizontally),
                    color1 = Color.Green,
                    color2 = Color.Red,
                    onTimerEnd = { println("¡El tiempo terminó!") }
                )
                QuizQuestion(
                    question = question!!,
                    currentRound = currentRound,
                    selectedAnswer = selectedAnswer,
                    onAnswerSelected = { viewModel.onAnswerSelected(it) },
                    timeExpired = timeExpired,
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
fun QuizQuestion(
    question: QuestionCountry,
    currentRound: Int,
    timeExpired: Boolean,
    selectedAnswer: Countries?,
    onAnswerSelected: (Countries) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Round $currentRound / 10",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Which country does this flag belong to?",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        Image(
            painter = rememberAsyncImagePainter("https://flagsapi.com/${question.correctCountry.code}/flat/64.png"),
            contentDescription = "Country Flag",
            modifier = Modifier
                .size(128.dp)
                .padding(bottom = 16.dp)
        )
        question.options.forEach { option ->
            val buttonColor = when {
                timeExpired && option == question.correctCountry -> correct.copy(alpha = 0.5f)
                selectedAnswer == option -> if (option == question.correctCountry) correct else MaterialTheme.colorScheme.error
                selectedAnswer != null && option == question.correctCountry -> correct
                else -> MaterialTheme.colorScheme.primary
            }
            Button(
                onClick = {
                    if (selectedAnswer == null) {
                        onAnswerSelected(option)
                    }
                },
                enabled = !timeExpired && selectedAnswer == null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = Color.White,
                    disabledContainerColor = buttonColor,
                    disabledContentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)

            ) {
                Text(text = option.title, fontSize = 16.sp)
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
fun QuizResult(
    score: Int,
    totalRounds: Int,
    navController: NavController,
    viewModel: FlagViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.saveResult(score, totalRounds)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Quiz Completed!",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Your Score: $score / $totalRounds",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = { navController.navigate(QuizzesScreen.MenuScreen.route) }) {
            Text("Volver al menu")
        }
    }
}