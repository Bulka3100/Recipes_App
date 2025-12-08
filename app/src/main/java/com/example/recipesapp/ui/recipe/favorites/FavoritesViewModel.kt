package com.example.recipesapp.ui.recipe.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(recipesRepository: RecipesRepository) : ViewModel() {
    private val _uiState = MutableLiveData(FavoritesUiState())
    val uiState = _uiState
    val repository = recipesRepository

    data class FavoritesUiState(
        val recipes: List<Recipe> = emptyList()
    )

    fun loadFavorites() {
        viewModelScope.launch {
            val favorites = repository.getFavoriteRecipes()
            _uiState.value = FavoritesUiState(recipes = favorites)
        }
    }
}