package com.example.recipesapp.data.repository

import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import kotlin.coroutines.CoroutineContext

class RecipesRepository(
    private val recipesDao: RecipesDao,
    private val categoriesDao: CategoriesDao,
    private val recipesApiService: RecipesApiService,
    private val ioDispatcher: CoroutineContext

) {


    suspend fun getCategoriesFromCache(): List<Category> {
        return categoriesDao.getAll()
    }

    suspend fun getCategories(): ApiResult<List<Category>> {
        return withContext(ioDispatcher) {
            try {
                val response = recipesApiService.getCategories().execute()
                if (response.isSuccessful) {
                    response.body()?.let { categories ->
                        categoriesDao.insertCategory(categories)
                        ApiResult.Success(categories)
                    } ?: ApiResult.Failure(IllegalStateException("Body is null"))
                } else {
                    ApiResult.Failure(Exception("Response code: ${response.code()}"))
                }
            } catch (e: Exception) {
                ApiResult.Failure(e)
            }
        }
    }

    suspend fun getRecipeById(id: Int): ApiResult<Recipe> {
        return withContext(ioDispatcher) {
            try {
                val response = recipesApiService.getRecipeById(id).execute()
                if (response.isSuccessful) {
                    response.body()?.let { recipe ->
                        recipesDao.insertRecipes(listOf(recipe))
                        ApiResult.Success(recipe)
                    } ?: ApiResult.Failure(IllegalStateException("Body is null"))
                } else {
                    ApiResult.Failure(Exception("Response code: ${response.code()}"))
                }
            } catch (e: Exception) {
                ApiResult.Failure(e)
            }
        }
    }

    suspend fun getRecipesByIds(ids: List<Int>): ApiResult<List<Recipe>> {
        return withContext(ioDispatcher) {
            try {
                val response = recipesApiService.getRecipes(ids).execute()
                if (response.isSuccessful) {
                    response.body()?.let { recipes ->
                        recipesDao.insertRecipes(recipes)
                        ApiResult.Success(recipes)
                    } ?: ApiResult.Failure(IllegalStateException("Body is null"))
                } else {
                    ApiResult.Failure(Exception("Response code: ${response.code()}"))
                }
            } catch (e: Exception) {
                ApiResult.Failure(e)
            }
        }
    }

    suspend fun getRecipeByIdFromCache(id: Int): Recipe? {
        return withContext(ioDispatcher) {
            recipesDao.getRecipeById(id)
        }
    }

    suspend fun getRecipesListCache(categoryId: Int): List<Recipe> {
        return withContext(ioDispatcher) {
            recipesDao.getRecipesByCategoryId(categoryId = categoryId)
        }
    }

    suspend fun getCategoryById(id: Int): ApiResult<Category> {
        return withContext(ioDispatcher) {
            try {
                val response = recipesApiService.getCategoryById(id).execute()
                if (response.isSuccessful) {
                    response.body()?.let { category ->
                        categoriesDao.insertCategory(listOf(category))
                        ApiResult.Success(category)
                    } ?: ApiResult.Failure(IllegalStateException("Body is null"))
                } else {
                    ApiResult.Failure(Exception("Response code: ${response.code()}"))
                }
            } catch (e: Exception) {
                ApiResult.Failure(e)
            }
        }
    }

    suspend fun updateFavorite(recipeId: Int, isFavorite: Boolean) {
        val recipe = recipesDao.getRecipeById(recipeId)
        recipe?.let {
            val updatedRecipe = it.copy(isFavorite = isFavorite)
            recipesDao.updateRecipe(updatedRecipe)
        }
    }

    suspend fun getRecipesByCategoryId(id: Int): ApiResult<List<Recipe>> {
        return withContext(ioDispatcher) {
            try {
                val response = recipesApiService.getRecipesByCategoryId(id).execute()
                if (response.isSuccessful) {
                    response.body()?.let { recipes ->
                        val recipesWithCategory = recipes.map { recipe ->
                            recipe.copy(categoryId = id)
                        }
                        recipesDao.insertRecipes(recipesWithCategory)
                        ApiResult.Success(recipes)
                    } ?: ApiResult.Failure(IllegalStateException("Body is null"))
                } else {
                    ApiResult.Failure(Exception("Response code: ${response.code()}"))
                }
            } catch (e: Exception) {
                ApiResult.Failure(e)

            }
        }
    }

    suspend fun getFavoriteRecipes(): List<Recipe> {
        return withContext(ioDispatcher) {
            recipesDao.getFavorites()
        }
    }

    sealed class ApiResult<out T> {
        data class Success<T>(val data: T) : ApiResult<T>()
        data class Failure(val exception: Throwable) : ApiResult<Nothing>()
    }

    suspend fun getAllRecipes(): ApiResult<List<Recipe>> {
        return withContext(ioDispatcher) {
            try {
                val response = recipesApiService.getRecipes(emptyList()).execute()
                if (response.isSuccessful) {
                    response.body()?.let { recipes ->
                        recipesDao.insertRecipes(recipes)
                        ApiResult.Success(recipes)
                    } ?: ApiResult.Failure(IllegalStateException("Body is null"))
                } else {
                    ApiResult.Failure(Exception("Response code: ${response.code()}"))
                }
            } catch (e: Exception) {
                ApiResult.Failure(e)
            }
        }
    }
}