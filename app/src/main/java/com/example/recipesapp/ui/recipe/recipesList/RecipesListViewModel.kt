package com.example.recipesapp.ui.recipe.recipesList

import android.app.Application
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
    val repository = RecipesRepository(application)

    fun loadRecipes(categoryId: Int) {
        viewModelScope.launch {
            //вот сюда можно добавить кэширование, но нет категории в сущности
            val result = repository.getRecipesByCategoryId(categoryId)
            val safeRecipes = when (result) {
                is RecipesRepository.ApiResult.Success -> result.data
                is RecipesRepository.ApiResult.Failure -> emptyList()
            }
            _recipes.value = safeRecipes
        }
    }
}