package com.example.recipesapp.model

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromIngredientsList(ingredients: List<Ingredient>): String {
        return json.encodeToString(ingredients)
    }

    @TypeConverter
    fun toIngredientsList(jsonString: String): List<Ingredient> {
        return json.decodeFromString(jsonString)
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return json.encodeToString(list)
    }

    @TypeConverter
    fun toStringList(jsonString: String): List<String> {
        return json.decodeFromString(jsonString)
    }
}