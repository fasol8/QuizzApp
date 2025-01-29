package com.sol.quizzapp.presentation.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sol.quizzapp.R
import java.util.Locale

@Composable
fun DifficultyBox(difficultSelected: MutableState<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        val difficulties = listOf("easy", "medium", "hard")

        Column {
            val iconDifficult = when (difficultSelected.value) {
                "hard" -> R.drawable.hard
                "medium" -> R.drawable.medium
                "easy" -> R.drawable.easy
                else -> R.drawable.difficult_default
            }
            Text("Difficult selected")
            Spacer(Modifier.height(4.dp))
            Icon(
                painter = painterResource(iconDifficult),
                contentDescription = "difficult selected",
                modifier = Modifier.size(100.dp),
            )
            Spacer(Modifier.height(4.dp))
            Text(text = difficultSelected.value)
        }
        Column() {
            difficulties.forEach { difficulty ->
                val colorSelected = if (difficultSelected.value == difficulty)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.secondary
                Button(
                    onClick = { difficultSelected.value = difficulty },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorSelected,
                        contentColor = Color.White,
                        disabledContainerColor = colorSelected,
                        disabledContentColor = Color.White
                    )
                ) {
                    Text(difficulty.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
                }
            }
        }
    }
}