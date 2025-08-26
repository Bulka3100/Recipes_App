package com.example.recipesapp.ui.recipe.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.KEY_FAVORITES
import com.example.recipesapp.PREFS_NAME
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableLiveData(FavoritesUiState())
    val uiState = _uiState
    val repository = RecipesRepository()
    val sharedPrefs by lazy {
        application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    data class FavoritesUiState(
        val recipes: List<Recipe> = emptyList()
    )

    fun loadFavorites() {
        viewModelScope.launch {
            val favorites = getFavorites()
            val favoriteIds = favorites.mapNotNull { it.toIntOrNull() }
            val result = repository.getRecipesByIds(favoriteIds)
            val safeFavoriteRecipes = when (result) {
                is RecipesRepository.ApiResult.Success -> result.data
                is RecipesRepository.ApiResult.Failure -> emptyList()
            }
            _uiState.value = FavoritesUiState(recipes = safeFavoriteRecipes)
        }
    }


    fun getFavorites(): MutableSet<String> {
        val getId = sharedPrefs.getStringSet(KEY_FAVORITES, emptySet<String>())
        return HashSet(getId ?: emptySet())
    }


}