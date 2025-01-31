package com.sol.quizzapp.presentation.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.sol.quizzapp.navigation.QuizzesScreen
import com.sol.quizzapp.ui.theme.correct

@Composable
fun <T> QuizQuestion(
    questionText: String,
    imageUrl: String?,
    options: List<T>,
    correctAnswer: T,
    selectedAnswer: T?,
    onAnswerSelected: (T) -> Unit,
    timeExpired: Boolean,
    currentRound: Int? = null,
    remainingTime: Int? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        currentRound?.let {
            Text(
                text = "Round $it / 10",
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        remainingTime?.let {
            CircularTimer(
                it,
                currentRound ?: 1,
                Modifier.size(96.dp),
                color1 = Color.Green,
                color2 = Color.Red,
                onTimerEnd = { println("¡Time ended!") }
            )
        }

        Text(
            text = questionText,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        imageUrl?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Question Image",
                modifier = Modifier
                    .size(128.dp)
                    .padding(bottom = 16.dp)
            )
        }

        options.forEach { option ->
            val buttonColor = when {
                timeExpired && option == correctAnswer -> correct.copy(alpha = 0.5f)
                selectedAnswer == option -> if (option == correctAnswer) correct else MaterialTheme.colorScheme.error
                selectedAnswer != null && option == correctAnswer -> correct
                else -> MaterialTheme.colorScheme.primary
            }

            Button(
                onClick = { if (selectedAnswer == null) onAnswerSelected(option) },
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
                Text(text = option.toString(), fontSize = 16.sp)
            }
        }

        if (timeExpired) {
            Text(
                "¡Time expired!",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ResultScreen(
    navController: NavController,
    title: String,
    message: String,
    score: Int,
    totalRounds: Int,
    onSaveResult: () -> Unit
) {
    LaunchedEffect(Unit) {
        onSaveResult()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Score: $score / $totalRounds",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(32.dp))
        Button(onClick = { navController.navigate(QuizzesScreen.MenuScreen.route) }) {
            Text("Back to menu")
        }
    }
}
