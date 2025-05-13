package com.example.recipesapp.ui.recipe.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.model.Recipe

class RecipeViewModel : ViewModel() {
    private val _recipeState = MutableLiveData<RecipeUiState>()

    // плохо понимаю зачем тут нужно две переменных смысл? посню с binding так делали, там вроде понятно, могла ошибка высветиться. Ксати тут надо обрабатывать ошибку с элвисом? вроде не может быть null а если надо , то как? спасибо
    val recipeState: LiveData<RecipeUiState> = _recipeState
        //тут пока оставлю такие комменты, потом начну оставлять другие. Почему бы не использовать тут get() ? как в binding


    data class RecipeUiState(
        val isFavorite: Boolean = false,
        val recipe: Recipe? = null,
        val portionsCount: Int = 1
    )

    init {
        _recipeState.value = RecipeUiState(isFavorite = false)
        Log.i("!!!", "initialized")
    }
}