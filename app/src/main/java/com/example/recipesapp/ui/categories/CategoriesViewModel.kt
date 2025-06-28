package com.example.recipesapp.ui.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipesapp.data.STUB
import com.example.recipesapp.model.Category

class CategoriesViewModel : ViewModel() {
    private val _categoryState = MutableLiveData(CategoriesUiState())
    val categoryState = _categoryState

    data class CategoriesUiState(
        val categoriesList: List<Category> = emptyList()
    )


    fun loadCategories(){
        _categoryState.value = _categoryState.value?.copy(categoriesList = STUB.getCategories())
    }
}