package com.sol.quizzapp.presentation.logo

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.sol.quizzapp.BuildConfig
import com.sol.quizzapp.R
import com.sol.quizzapp.domain.model.logo.QuestionCompany
import com.sol.quizzapp.domain.model.util.DifficultMode
import com.sol.quizzapp.presentation.utils.QuizQuestion
import com.sol.quizzapp.presentation.utils.ResultScreen
import com.sol.quizzapp.presentation.utils.playSound
import com.sol.quizzapp.presentation.utils.vibrate

@Composable
fun LogoScreen(
    navController: NavController,
    category: String,
    difficult: DifficultMode,
    viewModel: LogoViewModel = hiltViewModel()
) {
    val question by viewModel.currentQuestion.collectAsState()
    val currentRound by viewModel.currentRound.collectAsState()
    val score by viewModel.score.collectAsState()
    val quizFinished by viewModel.quizFinished.collectAsState()
    val remainingTime by viewModel.remainingTime.collectAsState()
    val selectedAnswer by viewModel.selectedAnswer.collectAsState(null)
    val nextButtonEnabled by viewModel.nextButtonEnabled.collectAsState()
    val timeExpired by viewModel.timeExpired.collectAsState(false)
    val gameMode by viewModel.gameMode.collectAsState()
    val userInput by viewModel.userInput.collectAsState()
    val attemptsLeft by viewModel.attemptsLeft.collectAsState()
    val hint by viewModel.hint.collectAsState()
    val totalRounds by viewModel.totalRounds.collectAsState()
    val context = LocalContext.current


    LaunchedEffect(category) {
        viewModel.selectCategory(category, difficult.value)
    }
    LaunchedEffect(remainingTime, selectedAnswer) {
        if (remainingTime > 0 && selectedAnswer == null) {
            kotlinx.coroutines.delay(1000)
            viewModel.decrementTimer()
        }
    }

    LaunchedEffect(selectedAnswer) {
        selectedAnswer?.let { answer ->
            if (question!!.correctCompany == answer) {
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
        LogoResultScreen(score = score, totalRounds, navController, viewModel)
    } else if (question != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Spacer(Modifier.heightIn(64.dp))

                Text(
                    text = "Round: $currentRound / ${totalRounds}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Spacer(Modifier.height(16.dp))
                if (gameMode == DifficultMode.EASY || gameMode == DifficultMode.MEDIUM) {
                    QuizQuestion(
                        questionText = "Which company does this logo belong to?",
                        imageUrl = "https://img.logo.dev/${question!!.correctCompany.domain}?token=${BuildConfig.LOGO_API_KEY}",
                        options = question!!.options,
                        correctAnswer = question!!.correctCompany,
                        selectedAnswer = selectedAnswer,
                        onAnswerSelected = { viewModel.onAnswerSelected(it) },
                        timeExpired = timeExpired,
                        remainingTime = remainingTime,
                        currentRound = currentRound,
                    )
                } else if (gameMode == DifficultMode.HARD) {
                    LogoInputQuestion(
                        userInput,
                        hint,
                        attemptsLeft,
                        viewModel,
                        nextButtonEnabled,
                        question!!
                    )
                }
            }

            if (gameMode == DifficultMode.EASY || gameMode == DifficultMode.MEDIUM) {
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
}

@Composable
fun LogoInputQuestion(
    userInput: String,
    hint: String,
    attemptsLeft: Int,
    viewModel: LogoViewModel,
    nextButtonEnabled: Boolean,
    question: QuestionCompany
) {
    Column {
        Image(
            painter = rememberAsyncImagePainter("https://img.logo.dev/${question.correctCompany.domain}?token=${BuildConfig.LOGO_API_KEY}"),
            contentDescription = "Country Flag",
            modifier = Modifier
                .size(128.dp)
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = question.correctCompany.category,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Enter the company name",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = userInput,
            onValueChange = { viewModel.onUserInputChanged(it) },
            label = { Text("Enter name") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                viewModel.checkAnswer()
            })
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Remaining attempts: $attemptsLeft",
            fontSize = 16.sp,
            color = Color.Gray
        )

        if (hint.isNotEmpty()) {
            Text(
                text = "Hints: $hint",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { viewModel.checkAnswer() },
            modifier = Modifier.fillMaxWidth(),
            enabled = userInput.isNotBlank() && !nextButtonEnabled
        ) {
            Text("Verify")
        }
    }
}

@Composable
fun LogoResultScreen(
    score: Int,
    totalRounds: Int,
    navController: NavController,
    viewModel: LogoViewModel
) {
    ResultScreen(
        navController = navController,
        title = "¡Logo Quiz Finish!",
        message = "You demonstrated your knowledge of logos",
        score = score,
        totalRounds = totalRounds,
        onSaveResult = { viewModel.saveResult(score, totalRounds) }
    )
}
