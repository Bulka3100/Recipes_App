package com.example.recipesapp.di

import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Category
import com.example.recipesapp.ui.categories.CategoriesViewModel

class CategoriesListViewModelFactory(
    private val recipesRepository: RecipesRepository
) : Factory<CategoriesViewModel> {
    override fun create(): CategoriesViewModel {
        return CategoriesViewModel(recipesRepository)
    }
}