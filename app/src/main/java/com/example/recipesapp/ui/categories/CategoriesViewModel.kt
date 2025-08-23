package com.example.recipesapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Category
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {
    private val _categoryState = MutableLiveData(CategoriesUiState())
    val categoryState : LiveData<CategoriesUiState> = _categoryState
    private val repository = RecipesRepository()

    data class CategoriesUiState(
        // плохо ли просто поставить List<Category>? как тип и почеуму?
        val categoriesList: List<Category> = emptyList()
    )


    fun loadCategories() {
        viewModelScope.launch {
            val safeCategory = repository.getCategories() ?: emptyList()
            _categoryState.value = categoryState.value?.copy(categoriesList = safeCategory)
        }
    }
}