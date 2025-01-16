package com.sol.quizzapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sol.quizzapp.presentation.QuizMenu
import com.sol.quizzapp.presentation.QuizScreen

@Composable
fun QuizNavHost(navController: NavHostController) {
    NavHost(navController, QuizzesScreen.QuizMenuScreen.route) {
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
    }
}