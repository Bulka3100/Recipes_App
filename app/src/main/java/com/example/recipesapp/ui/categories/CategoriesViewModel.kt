package com.example.recipesapp.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.recipesapp.data.repository.AppDataBase
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.model.Category
import kotlinx.coroutines.launch

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {
    private val _categoryState = MutableLiveData(CategoriesUiState())
    val categoryState: LiveData<CategoriesUiState> = _categoryState
    private val repository = RecipesRepository(application)
    val db = Room.databaseBuilder(
        application.applicationContext,
        AppDataBase::class.java,
        "databaseCategory"
    ).build()

    val categoriesDao = db.categoryDao()

    data class CategoriesUiState(
        // тут необходимо mutable live data&
        val categoriesList: List<Category> = emptyList()

    )


    fun loadCategories() {
        viewModelScope.launch {
            val cache = repository.getCategoriesFromCache()
            //просто продублировал, так и надо?
            _categoryState.value = categoryState.value?.copy(categoriesList = cache)
            val result = repository.getCategories()
            val safeCategory = when (result) {
                is RecipesRepository.ApiResult.Success -> result.data
                is RecipesRepository.ApiResult.Failure -> {
                    android.util.Log.d(
                        "FavoritesViewModel",
                        "Ошибка при получении категорий: ${result.exception.message}"
                    )
                    emptyList()
                }

            }
            categoriesDao.insertCategory(safeCategory)
            _categoryState.value = categoryState.value?.copy(categoriesList = safeCategory)
        }
    }
}