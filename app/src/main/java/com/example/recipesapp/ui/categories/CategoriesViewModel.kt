package com.example.recipesapp.ui.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.data.STUB
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Category

class CategoriesViewModel : ViewModel() {
    private val _categoryState = MutableLiveData(CategoriesUiState())
    val categoryState = _categoryState
    private val repository = RecipesRepository()

    data class CategoriesUiState(
        // плохо ли просто поставить List<Category>? как тип и почеуму?
        val categoriesList: List<Category> = emptyList()
    )


    fun loadCategories() {
        Thread{
            val safeCategory = repository.getCategories() ?: emptyList()
            _categoryState.value = _categoryState.value?.copy(categoriesList =safeCategory)

        }.start()
    }
}