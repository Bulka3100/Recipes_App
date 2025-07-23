package com.example.recipesapp.ui.recipe.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.KEY_FAVORITES
import com.example.recipesapp.PREFS_NAME
import com.example.recipesapp.data.STUB
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Recipe

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

    // решил все объеденить в одну функцию потому что не могу без callback из одного потока делать return  в другой. Хорошее временное решение?
    fun loadFavorites() {
        Thread {
            val favorites = getFavorites()
            val favoriteIds = favorites.mapNotNull { it.toIntOrNull() }
            val safeFavoriteRecipes = repository.getRecipesByIds(favoriteIds) ?: emptyList()
            _uiState.value = FavoritesUiState(recipes = safeFavoriteRecipes)

        }.start()
    }


    fun getFavorites(): MutableSet<String> {
        val getId = sharedPrefs.getStringSet(KEY_FAVORITES, emptySet<String>())
        return HashSet(getId ?: emptySet())
    }

    fun getRecipesById(id: Int): Recipe? {
        return STUB.getRecipeById(id)
    }
}