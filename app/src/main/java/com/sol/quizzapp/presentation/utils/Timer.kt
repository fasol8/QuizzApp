package com.sol.quizzapp.presentation.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun CircularTimer(
    totalTime: Int,
    questionIndex: Int,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 10f,
    color1: Color = MaterialTheme.colorScheme.primary,
    color2: Color = MaterialTheme.colorScheme.secondary,
    onTimerEnd: () -> Unit = {}
) {
    val timeLeft = remember(questionIndex) { mutableStateOf(totalTime) }
    val animatedProgress = remember { Animatable(1f) }

    LaunchedEffect(questionIndex) {
        animatedProgress.stop()
        animatedProgress.snapTo(1f)

        while (timeLeft.value > 0) {
            val startTime = System.currentTimeMillis()

            animatedProgress.animateTo(
                targetValue = timeLeft.value / totalTime.toFloat(),
                animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
            )

            val elapsedTime = System.currentTimeMillis() - startTime
            delay((1000L - elapsedTime).coerceAtLeast(0L))

            timeLeft.value--
        }
        if (timeLeft.value == 0) {
            animatedProgress.animateTo(0f, animationSpec = tween(500))
            onTimerEnd()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(1f)
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = if (timeLeft.value > 5) color1.copy(alpha = 0.3f) else color2.copy(alpha = 0.3f),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            drawArc(
                color = if (timeLeft.value > 5) color1 else color2,
                startAngle = -90f,
                sweepAngle = 360 * animatedProgress.value,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Text(
            text = "${timeLeft.value} s",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
