package com.example.recipesapp.data.repository

import com.example.recipesapp.BASE_URL
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class RecipesRepository() {
    val contentType = "application/json".toMediaType()
    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    val recipesApiService = retrofit.create(RecipesApiService::class.java)


    suspend fun getCategories(): List<Category>? {
        return withContext(Dispatchers.IO) {
            val call = recipesApiService.getCategories()
            try {
                val response = call.execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }


    suspend fun getRecipeById(id: Int): Recipe? {
        return withContext(Dispatchers.IO) {
            val call = recipesApiService.getRecipeById(id)
            try {
                val response = call.execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipesByIds(ids: List<Int>): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            val call = recipesApiService.getRecipes(ids)
            try {
                val response = call.execute()
                if (response.isSuccessful) response.body() else null
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getCategoryById(id: Int): Category? {
        return withContext(Dispatchers.IO) {
            val call = recipesApiService.getCategoryById(id)
            try {
                val response = call.execute()
                if (response.isSuccessful) response.body() else null
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipesByCategoryId(id: Int): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            val call = recipesApiService.getRecipesByCategoryId(id)
            try {
                val response = call.execute().body()
                response
            } catch (e: Exception) {
                null
            }
        }
    }
}