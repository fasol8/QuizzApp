package com.sol.quizzapp.navigation

sealed class QuizzesScreen(val route: String, val label: String) {

    data object MenuScreen : QuizzesScreen("menuScreen", "Menu Screen")
    data object QuizMenuScreen : QuizzesScreen("quizMenuScreen", "Quiz Menu Screen")
    data object QuizScreen : QuizzesScreen("quizScreen", "Quiz Screen")
    data object FlagScreen : QuizzesScreen("flagScreen", "Flag Screen")
    data object WordleMenuScreen : QuizzesScreen("wordleMenuScreen", "Wordle Menu Screen")
    data object WordleScreen : QuizzesScreen("wordleScreen", "Wordle Screen")
    data object LogoScreen : QuizzesScreen("logoScreen", "Logo Screen")
    data object ResultScreen : QuizzesScreen("resultScreen", "Result Screen")
}