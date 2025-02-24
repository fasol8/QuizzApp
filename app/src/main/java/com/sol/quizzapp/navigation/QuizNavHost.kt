package com.sol.quizzapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sol.quizzapp.domain.model.util.DifficultMode
import com.sol.quizzapp.presentation.MenuScreen
import com.sol.quizzapp.presentation.flag.FlagScreen
import com.sol.quizzapp.presentation.logo.LogoMenuScreen
import com.sol.quizzapp.presentation.logo.LogoScreen
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
            val difficultSelected = DifficultMode.fromValue(
                navBackStackEntry.arguments?.getString("difficult") ?: "easy"
            )
            QuizScreen(navController, categoryId, difficultSelected)
        }

        composable(QuizzesScreen.FlagScreen.route) { FlagScreen(navController) }

        composable(QuizzesScreen.WordleMenuScreen.route) { WordleMenu(navController) }
        composable(
            QuizzesScreen.WordleScreen.route + "/{difficulty}",
            arguments = listOf(navArgument("difficulty") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val difficultyString = navBackStackEntry.arguments?.getString("difficulty") ?: "easy"
            val difficulty = when (difficultyString) {
                "random" -> listOf(
                    DifficultMode.EASY,
                    DifficultMode.MEDIUM,
                    DifficultMode.HARD
                ).random()

                else -> DifficultMode.entries.find {
                    it.name.equals(difficultyString, ignoreCase = true)
                } ?: DifficultMode.EASY
            }
            WordleScreen(navController, difficulty)
        }

        composable(QuizzesScreen.LogoMenuScreen.route) { LogoMenuScreen(navController) }
        composable(
            QuizzesScreen.LogoScreen.route + "/{cat} - {difficult}",
            arguments = listOf(navArgument("cat") { type = NavType.StringType },
                navArgument("difficult") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val categoryString = navBackStackEntry.arguments?.getString("cat") ?: "default"
            val difficultSelected = DifficultMode.fromValue(
                navBackStackEntry.arguments?.getString("difficult") ?: "easy"
            )
            LogoScreen(navController, categoryString, difficultSelected)
        }

        composable(QuizzesScreen.ResultScreen.route) { com.sol.quizzapp.presentation.results.ResultScreen() }
    }
}