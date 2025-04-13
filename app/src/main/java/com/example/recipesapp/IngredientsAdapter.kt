package com.example.recipesapp


import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.Data_Classes.Category
import com.example.recipesapp.Data_Classes.Ingredient
import com.example.recipesapp.databinding.ItemCategoryBinding
import com.example.recipesapp.databinding.ItemIngridientsBinding
import java.io.InputStream

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemIngridientsBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemIngridientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
val ingridient = dataSet[position]
        holder.binding.tvIngridient.text = ingridient.description
        holder.binding.tvQuantity.text = ingridient.quantity
        holder.binding.tvUnit.text = ingridient.unitOfMeasure
    }

    override fun getItemCount(): Int = dataSet.size
}