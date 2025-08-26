package com.example.recipesapp.ui.recipe.recipesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class RecipesListViewModel : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes
    val repository = RecipesRepository()

    fun loadRecipes(categoryId: Int) {
        viewModelScope.launch {
            val result = repository.getRecipesByCategoryId(categoryId)
            val safeRecipes = when (result) {
                is RecipesRepository.ApiResult.Success -> result.data
                is RecipesRepository.ApiResult.Failure -> emptyList()
            }
            _recipes.value = safeRecipes
        }
    }


}
