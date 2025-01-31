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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sol.quizzapp.R
import com.sol.quizzapp.domain.model.util.DifficultMode
import java.util.Locale

@Composable
fun DifficultyBox(difficultSelected: MutableState<DifficultMode>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val iconDifficult = when (difficultSelected.value) {
                DifficultMode.HARD -> R.drawable.hard
                DifficultMode.MEDIUM -> R.drawable.medium
                DifficultMode.EASY -> R.drawable.easy
            }
            Text("Difficult selected")
            Spacer(Modifier.height(4.dp))
            Icon(
                painter = painterResource(iconDifficult),
                contentDescription = "difficult selected",
                modifier = Modifier.size(100.dp),
            )
            Spacer(Modifier.height(4.dp))
            Text(text = difficultSelected.value.name)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DifficultMode.entries.forEach { difficulty ->
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
                    Text(
                        difficulty.name.lowercase()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
                }
            }
        }
    }
}