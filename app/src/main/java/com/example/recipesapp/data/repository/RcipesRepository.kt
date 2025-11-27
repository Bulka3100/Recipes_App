package com.example.recipesapp.data.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.recipesapp.BASE_URL
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class RecipesRepository(context: Context) {
    val contentType = "application/json".toMediaType()
    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    val recipesApiService = retrofit.create(RecipesApiService::class.java)
    val db = Room.databaseBuilder(
        context.applicationContext,
        AppDataBase::class.java,
        "databaseCategory"
    ).build()

    val categoriesDao = db.categoryDao()

    sealed class ApiResult<out T> {
        data class Success<T>(val data: T) : ApiResult<T>()
        data class Failure(val exception: Throwable) : ApiResult<Nothing>()
    }
// а разве не нужно это делать в корутине?
suspend fun getCategoriesFromCache(): List<Category> {
    return categoriesDao.getAll()
}
    suspend fun getCategories(): ApiResult<List<Category>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = recipesApiService.getCategories().execute()
                if (response.isSuccessful) {
                    response.body()?.let { ApiResult.Success(it) }
                        ?: ApiResult.Failure(IllegalStateException("Body is null"))
                } else {
                    ApiResult.Failure(Exception("Response code: ${response.code()}"))
                }
            } catch (e: Exception) {
                ApiResult.Failure(e)
            }
        }
    }

    suspend fun getRecipeById(id: Int): ApiResult<Recipe> {
        return withContext(Dispatchers.IO) {
            try {
                val response = recipesApiService.getRecipeById(id).execute()
                if (response.isSuccessful) {
                    response.body()?.let { ApiResult.Success(it) }
                        ?: ApiResult.Failure(IllegalStateException("Body is null"))
                } else {
                    ApiResult.Failure(Exception("Response code: ${response.code()}"))
                }
            } catch (e: Exception) {
                ApiResult.Failure(e)
            }
        }
    }

    suspend fun getRecipesByIds(ids: List<Int>): ApiResult<List<Recipe>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = recipesApiService.getRecipes(ids).execute()
                if (response.isSuccessful) {
                    response.body()?.let { ApiResult.Success(it) }
                        ?: ApiResult.Failure(IllegalStateException("Body is null"))
                } else {
                    ApiResult.Failure(Exception("Response code: ${response.code()}"))
                }
            } catch (e: Exception) {
                ApiResult.Failure(e)
            }
        }
    }

    suspend fun getCategoryById(id: Int): ApiResult<Category> {
        return withContext(Dispatchers.IO) {
            try {
                val response = recipesApiService.getCategoryById(id).execute()
                if (response.isSuccessful) {
                    response.body()?.let { ApiResult.Success(it) }
                        ?: ApiResult.Failure(IllegalStateException("Body is null"))
                } else {
                    ApiResult.Failure(Exception("Response code: ${response.code()}"))
                }
            } catch (e: Exception) {
                ApiResult.Failure(e)
            }
        }
    }

    suspend fun getRecipesByCategoryId(id: Int): ApiResult<List<Recipe>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = recipesApiService.getRecipesByCategoryId(id).execute()
                if (response.isSuccessful) {
                    response.body()?.let { ApiResult.Success(it) }
                        ?: ApiResult.Failure(IllegalStateException("Body is null"))
                } else {
                    ApiResult.Failure(Exception("Response code: ${response.code()}"))
                }
            } catch (e: Exception) {
                ApiResult.Failure(e)
            }
        }
    }
}
