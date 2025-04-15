package com.example.recipesapp


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.Data_Classes.Ingredient
import com.example.recipesapp.databinding.ItemIngredientsBinding

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    fun updateIngredients(progress: Int) {
        quantity = progress
    }

    var quantity: Int = 1

    class ViewHolder(val binding: ItemIngredientsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: Ingredient, quantity: Int) {
            ingredientTextView.text = ingredient.description
            val amount = ingredient.quantity.toIntOrNull() ?: 0
            val newAmount = (amount * quantity)
            //не думаю что лучший вариант
            val newAmountFormated = if (newAmount % 1.0 == 0.0) {
                newAmount.toString()
            } else {
                String.format("%.1f", newAmount)
            }
            quantityTextView.text = newAmountFormated
            unitOfMeasureTextView.text = ingredient.unitOfMeasure
        }

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
        val ingredient = dataSet[position]
        //вот так вроде неплохо получилось, спасибо)
        holder.bind(ingredient, quantity)
    }

    override fun getItemCount(): Int = dataSet.size
}