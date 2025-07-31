package com.example.recipesapp.ui.recipe.recipesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Recipe

class RecipesListViewModel : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes
    val repository = RecipesRepository()

    fun loadRecipes(categoryId: Int) {
        Thread{
            val safeRecipes = repository.getRecipesByCategoryId(categoryId) ?: emptyList()
            _recipes.value = safeRecipes
        }.start()
    }


}
