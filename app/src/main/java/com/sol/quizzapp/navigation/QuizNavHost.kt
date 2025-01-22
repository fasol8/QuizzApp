package com.sol.quizzapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sol.quizzapp.presentation.MenuScreen
import com.sol.quizzapp.presentation.flag.FlagScreen
import com.sol.quizzapp.presentation.quiz.QuizMenu
import com.sol.quizzapp.presentation.quiz.QuizScreen
import com.sol.quizzapp.presentation.wordle.WordleMenu
import com.sol.quizzapp.presentation.wordle.WordleScreen

@Composable
fun QuizNavHost(navController: NavHostController) {
    NavHost(navController, QuizzesScreen.MenuScreen.route) {
        composable(QuizzesScreen.MenuScreen.route) { MenuScreen(navController) }
        composable(QuizzesScreen.QuizMenuScreen.route) { QuizMenu(navController) }
        composable(QuizzesScreen.QuizScreen.route + "/{category}-{difficult}",
            arguments = listOf(
                navArgument("category") { type = NavType.IntType },
                navArgument("difficult") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val categoryId = navBackStackEntry.arguments?.getInt("category") ?: 0
            val difficultSelected =
                navBackStackEntry.arguments?.getString("difficult") ?: "easy"
            QuizScreen(navController, categoryId, difficultSelected)
        }
        composable(QuizzesScreen.FlagScreen.route) { FlagScreen(navController) }
        composable(QuizzesScreen.WordleMenuScreen.route) { WordleMenu(navController) }
        composable(
            QuizzesScreen.WordleScreen.route + "/{cat}",
            arguments = listOf(navArgument("cat") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val categoryString = navBackStackEntry.arguments?.getString("cat") ?: "default"
            WordleScreen(navController, categoryString)
        }
    }
}