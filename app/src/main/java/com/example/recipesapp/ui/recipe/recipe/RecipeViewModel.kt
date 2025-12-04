package com.example.recipesapp.ui.recipe.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.BASE_URL
import com.example.recipesapp.KEY_FAVORITES
import com.example.recipesapp.PREFS_NAME
import com.example.recipesapp.R
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream


class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipeState = MutableLiveData(RecipeUiState())
    val recipeState: LiveData<RecipeUiState> = _recipeState
    private val repository = RecipesRepository(application)
    private val sharedPrefs by lazy {
        application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    data class RecipeUiState(
        val isFavorite: Boolean = false,
        val recipe: Recipe? = null,
        val portionsCount: Int = 1,
        val recipeImageUrl: String? = null
    )

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            val cachedRecipe = repository.getRecipeByIdFromCache(recipeId)

            if (cachedRecipe != null) {
                val recipeUrl = "${BASE_URL}images/${cachedRecipe.imageUrl ?: ""}"
                val isFavorite = recipeId.toString() in getFavorites()
                _recipeState.value = RecipeUiState(
                    recipe = cachedRecipe,
                    isFavorite = isFavorite,
                    recipeImageUrl = recipeUrl
                )
            }

            val result = repository.getRecipeById(recipeId)
            when (result) {
                is RecipesRepository.ApiResult.Success -> {
                    val recipeUrl = "${BASE_URL}images/${result.data.imageUrl ?: ""}"
                    val isFavorite = recipeId.toString() in getFavorites()
                    _recipeState.value = RecipeUiState(
                        recipe = result.data,
                        isFavorite = isFavorite,
                        recipeImageUrl = recipeUrl
                    )
                }
                is RecipesRepository.ApiResult.Failure -> {
                    Log.d("RecipeViewModel", "Ошибка при получении рецепта: ${result.exception.message}")
                }
            }
        }
    }

    fun onChangePortions(progress: Int) {
        _recipeState.value = recipeState.value?.copy(portionsCount = progress)
    }

    private fun getFavorites(): MutableSet<String> {
        return HashSet(sharedPrefs.getStringSet(KEY_FAVORITES, emptySet<String>()) ?: emptySet())
    }

    private fun saveFavorites(set: Set<String>) {
        sharedPrefs.edit().putStringSet(KEY_FAVORITES, set).apply()
    }

    fun onFavoriteClicked(recipeId: Int) {
        val updatedFavorites = getFavorites()
        if (updatedFavorites.contains(recipeId.toString())) {
            updatedFavorites.remove(recipeId.toString())
        } else {
            updatedFavorites.add(recipeId.toString())
        }
        saveFavorites(updatedFavorites)
        _recipeState.value = recipeState.value?.copy(isFavorite = recipeId.toString() in updatedFavorites)
    }
}