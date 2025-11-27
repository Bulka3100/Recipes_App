package com.example.recipesapp.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipesapp.model.Recipe

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipes")
    suspend fun getAll(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<Recipe>)
}