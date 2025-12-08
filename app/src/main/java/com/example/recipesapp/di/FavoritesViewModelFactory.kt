package com.example.recipesapp.di

import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.ui.recipe.favorites.FavoritesViewModel

class FavoritesViewModelFactory(private val recipesRepository: RecipesRepository) :
    Factory<FavoritesViewModel> {
    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(recipesRepository)
    }
}
