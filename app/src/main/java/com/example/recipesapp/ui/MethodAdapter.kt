package com.example.recipesapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.databinding.ItemMethodBinding

class MethodAdapter(dataSet: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {
    var dataSet: List<String> = dataSet
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(val binding: ItemMethodBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMethodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val step = dataSet[position]
        with(holder.binding) {

            tvText.text = "${position + 1}." + step
        }
    }

    override fun getItemCount(): Int = dataSet.size
}