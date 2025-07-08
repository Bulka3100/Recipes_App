package com.example.recipesapp.ui.recipe.recipesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.databinding.ItemRecipeBinding

class RecipesListAdapter(private var recipes: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    fun upgradeData(newData: List<Recipe>) {
        recipes = newData
        notifyDataSetChanged()
    }
    fun updateData(newList: List<Recipe>) {
        recipes = newList
        notifyDataSetChanged()
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.tvRecipeName.text = recipe.title

            val inputStream = binding.root.context.assets.open(recipe.imageUrl)
            binding.ivRecipe.setImageDrawable(
                android.graphics.drawable.Drawable.createFromStream(
                    inputStream,
                    null
                )
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
        holder.binding.ivRecipe.contentDescription =
            "Изображение рецепта ${holder.binding.tvRecipeName.text}"
        holder.binding.root.setOnClickListener {
            itemClickListener?.onItemClick(recipe.id)
        }
    }

    override fun getItemCount(): Int = recipes.size
}