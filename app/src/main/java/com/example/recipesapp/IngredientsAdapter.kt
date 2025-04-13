package com.example.recipesapp


import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.Data_Classes.Category
import com.example.recipesapp.Data_Classes.Ingredient
import com.example.recipesapp.databinding.ItemCategoryBinding
import com.example.recipesapp.databinding.ItemIngredientsBinding
import java.io.InputStream

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemIngredientsBinding) : RecyclerView.ViewHolder(binding.root) {
        val ingredientTextView = binding.tvIngridient
        val quantityTextView = binding.tvQuantity
        val unitOfMeasureTextView = binding.tvUnit
//        а зачем так делать?
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemIngredientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingridient = dataSet[position]
        //нужно было вынести переменные выше чтобы тут удобнее обозначить? А не легче бы было использовать конструкцию with или вообще отдельную функцию bind создать? Как правильно?
        holder.ingredientTextView.text = ingridient.description
        holder.quantityTextView.text = ingridient.quantity
        holder.unitOfMeasureTextView.text = ingridient.unitOfMeasure
    }

    override fun getItemCount(): Int = dataSet.size
}