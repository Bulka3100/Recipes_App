package com.example.recipesapp.data.repository

import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipesApiService {
    @GET("recipes")
    fun getRecipes(@Query("ids") ids:List<Int>): Call<List<Recipe>>

    @GET("category")
    fun getCategories(): Call<List<Category>>

    @GET("recipe/{id}")
    fun getRecipeById(@Path("id") id: Int): Call<Recipe>

    @GET("category/{id}")
    fun getCategoryById(@Path("id") id: Int): Call<Category>

    @GET("category/{id}/recipes")
    fun getRecipesByCategoryId(@Path("id") id: Int): Call<List<Recipe>>
}
