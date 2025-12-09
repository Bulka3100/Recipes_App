package com.example.recipesapp.ui.categories

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ItemCategoryBinding
import com.example.recipesapp.model.Category

class CategoriesListAdapter(private var dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    fun updateData(newItems: List<Category>) {
        dataSet = newItems
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category, context: Context) {
            binding.tvNameCt.text = category.title
            binding.tvDescription.text = category.description

            val imageUrl = if (category.imageUrl?.startsWith("http") == true) {
                category.imageUrl
            } else {
                "https://recipes.androidsprint.ru/api/images/${category.imageUrl}"
            }
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.ivCategories)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = dataSet[position]
        holder.bind(category, holder.itemView.context)

        holder.binding.root.setOnClickListener {
            itemClickListener?.onItemClick(category.id)
        }
    }

    override fun getItemCount(): Int = dataSet.size
}
