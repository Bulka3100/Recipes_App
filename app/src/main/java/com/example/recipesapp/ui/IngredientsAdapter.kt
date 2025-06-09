package com.example.recipesapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.model.Ingredient
import com.example.recipesapp.databinding.ItemIngredientsBinding
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter(dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    var dataSet: List<Ingredient> = dataSet
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }

    var quantity: Int = 1

    class ViewHolder(val binding: ItemIngredientsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: Ingredient, quantity: Int) {
            ingredientTextView.text = ingredient.description
            // вроде такая запись адекватно выглядит. Подойдет такой вариант?
            val amount = ingredient.quantity.toBigDecimalOrNull() ?: BigDecimal.ZERO

            val totalQuantity = amount * BigDecimal(quantity)
            val displayQuantity = totalQuantity
                .setScale(1, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString()

            quantityTextView.text = displayQuantity
            unitOfMeasureTextView.text = ingredient.unitOfMeasure
        }

        val ingredientTextView = binding.tvIngridient
        val quantityTextView = binding.tvQuantity
        val unitOfMeasureTextView = binding.tvUnit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemIngredientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = dataSet[position]
        holder.bind(ingredient, quantity)
    }

    override fun getItemCount(): Int = dataSet.size
}