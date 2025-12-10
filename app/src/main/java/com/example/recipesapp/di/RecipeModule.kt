package com.example.recipesapp.di

import android.content.Context
import androidx.room.Room
import com.example.recipesapp.BASE_URL
import com.example.recipesapp.data.repository.AppDataBase
import com.example.recipesapp.data.repository.CategoriesDao
import com.example.recipesapp.data.repository.RecipesApiService
import com.example.recipesapp.data.repository.RecipesDao
import com.example.recipesapp.data.repository.RecipesRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class RecipeModule {

    @Provides
    fun provideDatabse(@ApplicationContext context: Context): AppDataBase =
        Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "databaseCategory"
        ).build()

    @Provides
    fun provideCategoriesDao(appDataBase: AppDataBase): CategoriesDao = appDataBase.categoryDao()

    @Provides
    fun provideRecipesDao(appDataBase: AppDataBase): RecipesDao = appDataBase.recipesDao()

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(logging).build()

    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .client(okHttpClient).build()

    }

    @Provides
    fun provideRecipesApiService(retrofit: Retrofit): RecipesApiService {
        return retrofit.create(RecipesApiService::class.java)
    }

}