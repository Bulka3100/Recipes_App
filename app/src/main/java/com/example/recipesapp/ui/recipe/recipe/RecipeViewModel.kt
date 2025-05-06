package com.example.recipesapp.ui.recipe.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.model.Recipe

class RecipeViewModel : ViewModel() {
    private val savedState: MutableLiveData<RecipeUiState>? = null

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
}