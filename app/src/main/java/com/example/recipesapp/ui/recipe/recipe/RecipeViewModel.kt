package com.example.recipesapp.ui.recipe.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.R
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.ui.recipe.recipe.RecipeFragment.Companion.KEY_FAVORITES
import com.example.recipesapp.ui.recipe.recipe.RecipeFragment.Companion.PREFS_NAME

class RecipeViewModel : AndroidViewModel(application = Application()) {
    private val savedState: MutableLiveData<RecipeUiState>? = null
    private val sharedPrefs by lazy {
        //проблема с видимостью
        application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // плохо понимаю зачем тут нужно две переменных смысл? посню с binding так делали, там вроде понятно, могла ошибка высветиться. Ксати тут надо обрабатывать ошибку с элвисом? вроде не может быть null а если надо , то как? спасибо
    val _savedState: LiveData<RecipeUiState>
        get() = savedState ?: throw IllegalStateException("Null state")

    data class RecipeUiState(
        val isFavorite: Boolean = false,
        val recipe: Recipe? = null,
        val portionsCount: Int = 1
    )

    init {
        savedState?.value = RecipeUiState(isFavorite = false)
        Log.i("!!!", "initialized")

    }

    fun loadRecipe(recipeId: Int) {
//        TODO  'load from network'
        val recipe = STUB.getRecipeById(recipeId)

        val isFavorite = recipeId.toString() in getFavorites()
        savedState?.value = RecipeUiState(
            recipe = recipe,
            isFavorite = isFavorite,
            //как взять значение для порций из текущего стйта? так можно?
            portionsCount = RecipeUiState().portionsCount
        )

    }

    private fun getFavorites(): MutableSet<String> {
        return HashSet(sharedPrefs.getStringSet(KEY_FAVORITES, emptySet<String>()) ?: emptySet())
    }

    private fun saveFavorites(set: Set<String>) {
        sharedPrefs.edit().putStringSet(KEY_FAVORITES, set).commit()
    }

    fun onFavoriteClicked(recipeId: Int) {

        val updatedFavorites = getFavorites()
        if (updatedFavorites.contains(recipeId.toString())) {
            updatedFavorites.remove(recipeId.toString())
        } else {
            updatedFavorites.add((recipeId.toString()))
        }

        saveFavorites(updatedFavorites)
    }
}
