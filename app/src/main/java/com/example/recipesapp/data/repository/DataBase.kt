package com.example.recipesapp.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipesapp.model.Category

@Database(entities = [Category::class], version = 1)
abstract class AppDataBase: RoomDatabase(){

    abstract fun categoryDao() : CategoriesDao
}