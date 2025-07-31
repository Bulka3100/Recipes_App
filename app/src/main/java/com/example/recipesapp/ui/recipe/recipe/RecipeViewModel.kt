package com.example.recipesapp.ui.recipe.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.KEY_FAVORITES
import com.example.recipesapp.PREFS_NAME
import com.example.recipesapp.data.STUB
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Recipe
import java.io.IOException
import java.io.InputStream


class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipeState = MutableLiveData(RecipeUiState())
    val recipeState: LiveData<RecipeUiState> = _recipeState
    private val repository = RecipesRepository()
    private val sharedPrefs by lazy {

        application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }


    data class RecipeUiState(
        val isFavorite: Boolean = false,
        val recipe: Recipe? = null,
        val portionsCount: Int = 1,
        val recipeImage: Drawable? = null
    )

    fun loadRecipe(recipeId: Int) {

        Thread {
            val safeRecipe = repository.getRecipeById(recipeId)
            val drawable = if (safeRecipe?.imageUrl != null) {
                try {
                    val inputStream: InputStream =
                        getApplication<Application>().assets.open(safeRecipe.imageUrl)
                    Drawable.createFromStream(inputStream, null).also {
                        inputStream.close()
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

            _recipeState.value = recipeState.value?.copy(
                recipe = safeRecipe,
                isFavorite = isFavorite,
                recipeImage = drawable,
            )

        }.start()
    }


    fun onChangePortions(progress: Int) {
        _recipeState.value = recipeState.value?.copy(portionsCount = progress)
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
        //!!!
        recipeState.value?.copy(isFavorite = recipeId.toString() in getFavorites())
    }
}

