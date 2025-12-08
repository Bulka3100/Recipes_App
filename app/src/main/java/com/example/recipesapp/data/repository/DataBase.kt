package com.example.recipesapp.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipesapp.model.Category
import com.example.recipesapp.model.Converters
import com.example.recipesapp.model.Recipe

@Database(entities = [Category::class, Recipe::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDataBase: RoomDatabase(){

    abstract fun categoryDao() : CategoriesDao
    abstract fun recipesDao() : RecipesDao
}