package com.example.recipesapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor (
    private val repository: RecipesRepository) : ViewModel() {
    private val _categoryState = MutableLiveData(CategoriesUiState())
    val categoryState: LiveData<CategoriesUiState> = _categoryState


    data class CategoriesUiState(
        val categoriesList: List<Category> = emptyList()
    )

    fun loadCategories() {
        viewModelScope.launch {
            val cache = repository.getCategoriesFromCache()
            _categoryState.value = CategoriesUiState(categoriesList = cache)

            val result = repository.getCategories()
            val safeCategory = when (result) {
                is RecipesRepository.ApiResult.Success -> result.data
                is RecipesRepository.ApiResult.Failure -> {
                    android.util.Log.d(
                        "CategoriesViewModel",
                        "Ошибка при получении категорий: ${result.exception.message}"
                    )
                    emptyList()
                }
            }

            _categoryState.value = CategoriesUiState(categoriesList = safeCategory)
        }
    }
}