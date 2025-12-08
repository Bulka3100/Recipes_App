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


class RecipeViewModel(recipesRepository: RecipesRepository) : ViewModel() {
    private val _recipeState = MutableLiveData(RecipeUiState())
    val recipeState: LiveData<RecipeUiState> = _recipeState
    private val repository = recipesRepository


    data class RecipeUiState(
        val isFavorite: Boolean = false,
        val recipe: Recipe? = null,
        val portionsCount: Int = 1,
        val recipeImageUrl: String? = null
    )

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            val cachedRecipe = repository.getRecipeByIdFromCache(recipeId)
            cachedRecipe?.let { recipe ->
                val recipeUrl = "${BASE_URL}images/${recipe.imageUrl ?: ""}"
                _recipeState.value = RecipeUiState(
                    recipe = recipe,
                    isFavorite = recipe.isFavorite,
                    recipeImageUrl = recipeUrl
                )
            }

            val result = repository.getRecipeById(recipeId)
            when (result) {
                is RecipesRepository.ApiResult.Success -> {
                    val recipeUrl = "${BASE_URL}images/${result.data.imageUrl ?: ""}"

                    _recipeState.value = RecipeUiState(
                        recipe = result.data,
                        isFavorite = result.data.isFavorite,
                        recipeImageUrl = recipeUrl
                    )
                }

                is RecipesRepository.ApiResult.Failure -> {
                    Log.d(
                        "RecipeViewModel",
                        "Ошибка при получении рецепта: ${result.exception.message}"
                    )
                }
            }
        }
    }

    fun onChangePortions(progress: Int) {
        _recipeState.value = recipeState.value?.copy(portionsCount = progress)
    }

    fun onFavoriteClicked(recipeId: Int) {
        viewModelScope.launch {
            try {
                val currentIsFavorite = _recipeState.value?.isFavorite ?: false
                val newStatus = !currentIsFavorite
                repository.updateFavorite(recipeId, newStatus)

                _recipeState.value = _recipeState.value?.copy(isFavorite = newStatus)

            } catch (e: Exception) {
                Log.e("RecipeViewModel", "Ошибка", e)
            }
        }
    }

}