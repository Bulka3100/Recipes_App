package com.example.recipesapp.ui.recipe.recipe

import androidx.lifecycle.ViewModel
import com.example.recipesapp.model.Recipe

class RecipeViewModel : ViewModel() {
    data class RecipeUiState(
        val isFavorite: Boolean = false,
        val recipe: Recipe? = null,
        val portionsCount: Int = 1
    )
}