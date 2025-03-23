package com.example.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.databinding.RecipeFragmentBinding

class RecipeFragment :Fragment(){
    private var _binding: RecipeFragmentBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("пустой фрагмент")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecipeFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }
}
