package com.example.recipesapp.ui.recipe.recipesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Recipe

class RecipesListViewModel : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    fun loadRecipes(categoryId: Int) {
        _recipes.value = STUB.getRecipesByCategoryId(categoryId)
    }

    fun getRecipeById(id: Int): Recipe? {
        return STUB.getRecipeById(id)
    }
}
