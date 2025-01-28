package com.sol.quizzapp.presentation.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableCard(title: String, content: @Composable () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorCardType(title),
            contentColor = Color.White,
            disabledContainerColor = colorCardType(title),
            disabledContentColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = ""
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                content()
            }
        }
    }
}

private fun colorCardType(title: String): Color {
    return when (title) {
        "Quiz" -> Color.LightGray
        "Flag" -> Color.Gray
        "Wordle" -> Color.DarkGray
        "Logo" -> Color.Black
        else -> Color.Blue
    }
}

@Composable
fun <T> AnimatedLazyColumn(
    items: List<T>,
    content: @Composable (T) -> Unit
) {
    LazyColumn(
        modifier = Modifier.heightIn(max = 600.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(items) { index, item ->
            val alpha = remember { Animatable(0f) }
            val offset = remember { Animatable(50f) }

            LaunchedEffect(Unit) {
                alpha.animateTo(1f, animationSpec = tween(300, delayMillis = index * 100))
                offset.animateTo(0f, animationSpec = tween(300, delayMillis = index * 100))
            }

            Box(
                modifier = Modifier
                    .graphicsLayer(alpha = alpha.value, translationY = offset.value)
            ) {
                content(item)
            }
        }
    }
}