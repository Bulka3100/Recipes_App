package com.example.recipesapp.ui.recipe.recipe

import androidx.lifecycle.ViewModel
import com.example.recipesapp.model.Recipe

class RecipeViewModel : ViewModel() {
    data class RecipeUiState(
        var isFavorite: Boolean = false,
        var recipe: Recipe? = null,
        var recipePic: String? = null,
    )
}