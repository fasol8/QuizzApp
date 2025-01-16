package com.sol.quizzapp.navigation

sealed class QuizzesScreen(val route: String, val label: String) {

    data object QuizMenuScreen : QuizzesScreen("quizMenuScreen", "Quiz Menu Screen")
    data object QuizScreen : QuizzesScreen("quizScreen", "Quiz Screen")
}