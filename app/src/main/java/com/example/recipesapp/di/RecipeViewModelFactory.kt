package com.example.recipesapp.di

import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.ui.recipe.recipe.RecipeViewModel

class RecipeViewModelFactory(private val recipesRepository: RecipesRepository)
    : Factory<RecipeViewModel> {
    override fun create(): RecipeViewModel {
        return RecipeViewModel(recipesRepository)
    }
}