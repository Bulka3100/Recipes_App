package com.example.recipesapp.di

import android.content.Context
import androidx.room.Room
import com.example.recipesapp.BASE_URL
import com.example.recipesapp.data.repository.AppDataBase
import com.example.recipesapp.data.repository.RecipesApiService
import com.example.recipesapp.data.repository.RecipesRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class AppContainer(context: Context) {
    val logging = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    val client = OkHttpClient.Builder().addInterceptor(logging).build()
    val contentType = "application/json".toMediaType()

    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .client(client).build()
    val recipesApiService = retrofit.create(RecipesApiService::class.java)
    val db = Room.databaseBuilder(
        context,
        AppDataBase::class.java,
        "databaseCategory"
    ).build()
    val ioDispatcher = Dispatchers.IO
    val categoriesDao = db.categoryDao()
    val recipesDao = db.recipesDao()

    val repository = RecipesRepository(
        recipesDao = recipesDao,
        categoriesDao = categoriesDao,
        recipesApiService = recipesApiService,
        ioDispatcher = ioDispatcher
    )
    val recipeViewModelFactory = RecipeViewModelFactory(repository)
    val favoritesViewModelFactory = FavoritesViewModelFactory(repository)
    val recipesListViewModelFactory = RecipesListViewModelFactory(repository)
    val categoriesListViewModelFactory = CategoriesListViewModelFactory(repository)
}