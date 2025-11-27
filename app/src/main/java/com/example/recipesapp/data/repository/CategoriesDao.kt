package com.example.recipesapp.data.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipesapp.model.Category

@Dao
interface CategoriesDao{
    @Query("SELECT * FROM category")
    suspend fun getAll() : List<Category>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(categories : List<Category>)
}