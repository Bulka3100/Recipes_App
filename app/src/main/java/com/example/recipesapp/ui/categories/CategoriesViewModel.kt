package com.example.recipesapp.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Category
import kotlinx.coroutines.launch

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {
    private val _categoryState = MutableLiveData(CategoriesUiState())
    val categoryState: LiveData<CategoriesUiState> = _categoryState
    private val repository = RecipesRepository(application)

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

            repository.categoriesDao.insertCategory(safeCategory)
            _categoryState.value = CategoriesUiState(categoriesList = safeCategory)
        }
    }
}