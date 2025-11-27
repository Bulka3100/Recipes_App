package com.example.recipesapp.ui.recipe.recipesList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes
    private val repository = RecipesRepository(application)

    fun loadRecipes(categoryId: Int) {
        viewModelScope.launch {
            val cachedRecipes = repository.getAllRecipesFromCache()
            _recipes.value = cachedRecipes

            val result = repository.getRecipesByCategoryId(categoryId)
            val freshRecipes = when (result) {
                is RecipesRepository.ApiResult.Success -> result.data
                is RecipesRepository.ApiResult.Failure -> {
                    Log.d("RecipesListViewModel", "Ошибка при получении рецептов: ${result.exception.message}")
                    cachedRecipes
                }
            }
            _recipes.value = freshRecipes
        }
    }
}