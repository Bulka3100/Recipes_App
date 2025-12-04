package com.example.recipesapp.model
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
@Entity("recipes")
data class Recipe(
    @PrimaryKey
    val id: Int,
    val title: String,
    val ingredients: List<Ingredient>,
    val method: List<String>,
    val imageUrl: String,
    val isFavorite: Boolean,
    val categoryId : Int // не было! Тогда как вообще можно было фильтроваться по категории кроме как в апи? Правильно решил?
): Parcelable
