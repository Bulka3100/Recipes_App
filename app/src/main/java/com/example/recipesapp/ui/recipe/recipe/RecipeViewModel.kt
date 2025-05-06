package com.example.recipesapp.ui.recipe.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
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
import java.io.IOException
import java.io.InputStream

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val _savedState = MutableLiveData<RecipeUiState>()
    val savedState: LiveData<RecipeUiState> get() = _savedState
    private val sharedPrefs by lazy {

        application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // плохо понимаю зачем тут нужно две переменных смысл? посню с binding так делали, там вроде понятно, могла ошибка высветиться. Ксати тут надо обрабатывать ошибку с элвисом? вроде не может быть null а если надо , то как? спасибо


    data class RecipeUiState(
        val isFavorite: Boolean = false,
        val recipe: Recipe? = null,
        val portionsCount: Int = 1,
        val recipeImage: Drawable? = null
    )

    init {
        _savedState.value = RecipeUiState(isFavorite = false)
        Log.i("!!!", "initialized")

    }

    fun loadRecipe(recipeId: Int) {
//        TODO  'load from network'
        val recipe = STUB.getRecipeById(recipeId)
        val drawable = if (recipe?.imageUrl != null) {
            try {
                // Открываем InputStream из assets
                val inputStream: InputStream = getApplication<Application>().assets.open(recipe.imageUrl)
                // Создаём Drawable из InputStream
                Drawable.createFromStream(inputStream, null).also {
                    inputStream.close() // Закрываем поток
                }
            } catch (e: IOException) {
                Log.e("RecipeViewModel", "Error loading image from assets: ${e.message}")
                null
            }
        } else {
            Log.e("RecipeViewModel", "Image path is null")
            null
        }
        val isFavorite = recipeId.toString() in getFavorites()
        val portion = savedState.value?.portionsCount ?: RecipeUiState().portionsCount
        _savedState.value = RecipeUiState(
            recipe = recipe,
            isFavorite = isFavorite,
            //как взять значение для порций из текущего стйта? так можно?

            portionsCount = portion
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
