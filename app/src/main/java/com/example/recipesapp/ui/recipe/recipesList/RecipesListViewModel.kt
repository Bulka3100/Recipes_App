package com.example.recipesapp.ui.recipe.recipesList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesListViewModel @Inject constructor(recipesRepository: RecipesRepository) : ViewModel() {
    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes
    private val repository = recipesRepository

    fun loadRecipes(categoryId: Int) {
        viewModelScope.launch {
            val cachedRecipes = repository.getRecipesListCache(categoryId)
            _recipes.value = cachedRecipes

            val result = repository.getRecipesByCategoryId(categoryId)
            val freshRecipes = when (result) {
                is RecipesRepository.ApiResult.Success -> result.data
                is RecipesRepository.ApiResult.Failure -> {
                    Log.d(
                        "RecipesListViewModel",
                        "Ошибка при получении рецептов: ${result.exception.message}"
                    )
                    cachedRecipes
                }
            }
            _recipes.value = freshRecipes
        }
    }
}