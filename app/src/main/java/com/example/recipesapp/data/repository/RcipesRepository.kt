package com.example.recipesapp.data.repository

import com.example.recipesapp.BASE_URL
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import java.util.concurrent.Executors

class RecipesRepository() {
val contentType = "application/json".toMediaType()
    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    val recipesApiService = retrofit.create(RecipesApiService::class.java)


    fun getCategories(): List<Category>? {
        val call = recipesApiService.getCategories()
        return try {
            val response = call.execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    fun getRecipeById(id: Int): Recipe? {
        val call = recipesApiService.getRecipeById(id)
        return try {
            val response = call.execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    fun getRecipesByIds(ids: List<Int>): List<Recipe>? {
        val call = recipesApiService.getRecipes(ids)
        return try {
            val response = call.execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    fun getCategoryById(id: Int): Category? {
        val call = recipesApiService.getCategoryById(id)
        return try {
            val response = call.execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
           null
        }
    }

    fun getRecipesByCategoryId(id: Int): List<Recipe>? {
        val call = recipesApiService.getRecipesByCategoryId(id)
        return try {
            val response = call.execute().body()
            response
        } catch (e: Exception) {
            null
        }
    }

}