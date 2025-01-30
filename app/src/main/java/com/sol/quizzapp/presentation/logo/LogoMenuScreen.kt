package com.sol.quizzapp.presentation.logo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sol.quizzapp.domain.model.logo.Company
import com.sol.quizzapp.navigation.QuizzesScreen
import com.sol.quizzapp.presentation.utils.DifficultyBox

@Composable
fun LogoMenuScreen(navController: NavController) {

    val categories = Company.getCategories()
    val difficultSelected = remember { mutableStateOf("easy") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, start = 8.dp, end = 8.dp)
    ) {
        DifficultyBox(difficultSelected)
        Spacer(Modifier.height(4.dp))
        Button(
            onClick = { navController.navigate(QuizzesScreen.LogoScreen.route + "/Random - ${difficultSelected.value}") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Random")
        }
        Spacer(Modifier.height(4.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
        ) {
            items(categories.size) { index ->
                CategoryItem(categories[index]) {
                    navController.navigate(QuizzesScreen.LogoScreen.route + "/${categories[index]} - ${difficultSelected.value}")
                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(30.dp),
        onClick = { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(4.dp)
        )
    }
}
