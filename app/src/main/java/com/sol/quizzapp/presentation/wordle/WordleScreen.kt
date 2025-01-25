package com.sol.quizzapp.presentation.wordle

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sol.quizzapp.domain.model.wordle.WordleGameState
import com.sol.quizzapp.ui.theme.correct
import com.sol.quizzapp.ui.theme.present

@Composable
fun WordleScreen(
    navController: NavController,
    category: String,
    viewModel: WordleViewModel = hiltViewModel()
) {
    val gameState by viewModel.gameState.collectAsState()
    val targetWord = gameState.targetWord

    LaunchedEffect(category) {
        viewModel.startNewGame(category)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 48.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Crossfade(targetState = gameState.gameFinished) { gameFinished ->
            if (gameFinished) {
                WordleEndScreen(
                    gameWon = gameState.gameWon,
                    targetWord = gameState.targetWord,
                    attempts = gameState.attempts,
                    navController = navController,
                    viewModel = viewModel,
                    category = category
                )
            } else {
                WordleGameScreen(
                    currentGuess = gameState.currentGuess,
                    targetWord,
                    gameState,
                    viewModel,
                )
            }
        }
    }
}

@Composable
fun WordleGameScreen(
    currentGuess: String,
    targetWord: String,
    gameState: WordleGameState,
    viewModel: WordleViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.animateContentSize()) {
            if (gameState.guesses.isEmpty()) {
                LetterBoxes(
                    guess = "",
                    feedback = emptyList(),
                    targetWord = targetWord,
                    isPlaceholder = true
                )
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                gameState.guesses.forEachIndexed { index, guess ->
                    val feedback = gameState.feedback.getOrNull(index) ?: emptyList()
                    LetterBoxes(guess = guess, feedback = feedback, targetWord = targetWord)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        TextField(
            value = currentGuess,
            onValueChange = { newGuess ->
                if (newGuess.length <= targetWord.length) {
                    viewModel.updateCurrentGuess(newGuess)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter your guess") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.makeGuess(currentGuess)
                }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.makeGuess(currentGuess)
            },
            enabled = currentGuess.length == targetWord.length
        ) {
            Text(text = "Verify")
        }
    }
}

@Composable
fun LetterBoxes(
    targetWord: String,
    guess: String,
    feedback: List<String>,
    isPlaceholder: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (isPlaceholder) {
            repeat(targetWord.length) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .border(1.dp, Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "", style = MaterialTheme.typography.bodySmall)
                }
            }
        } else {
            guess.padEnd(targetWord.length, '-').forEachIndexed { index, char ->
                val targetColor = when (feedback.getOrNull(index)) {
                    "correct" -> correct
                    "present" -> present
                    "absent" -> Color.DarkGray
                    else -> Color.Transparent
                }
                val animatedColor by animateColorAsState(targetColor)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .border(1.dp, Color.Black)
                        .background(animatedColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (char != '-') char.toString() else "",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun WordleEndScreen(
    navController: NavController,
    gameWon: Boolean,
    targetWord: String,
    attempts: Int,
    category: String,
    viewModel: WordleViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.saveResult(targetWord, category, attempts, gameWon)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (gameWon) "¡Felicidades! 🎉" else "¡Inténtalo de nuevo!",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (gameWon) {
                "Acertaste la palabra \"$targetWord\" en $attempts intento(s)."
            } else {
                "La palabra era \"$targetWord\". Mejor suerte la próxima vez."
            },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { navController.navigate("MenuScreen") }) {
            Text("Volver al Menú")
        }
    }
}