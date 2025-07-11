package com.example.recipesapp.ui.categories

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.model.Category
import com.example.recipesapp.databinding.ItemCategoryBinding
import java.io.InputStream

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

            try {
                val imagePath = category.imageUrl
                val inputStream: InputStream = context.assets.open(imagePath)
                val drawable = Drawable.createFromStream(inputStream, null)

                if (drawable == null) {
                } else {
                    binding.ivCategories.setImageDrawable(drawable)
                }
                inputStream.close()
            } catch (e: Exception) {
            }
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