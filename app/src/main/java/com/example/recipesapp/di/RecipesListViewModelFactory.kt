package com.example.recipesapp.di

import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.ui.recipe.recipesList.RecipesListViewModel

class RecipesListViewModelFactory(private val recipesRepository: RecipesRepository) :
    Factory<RecipesListViewModel> {
    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(recipesRepository)
    }
}