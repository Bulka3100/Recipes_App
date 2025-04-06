package com.example.recipesapp

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipesapp.Data_Classes.Recipe
import com.example.recipesapp.RecipesListFragment.Companion.ARG_RECIPE
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.databinding.RecipeFragmentBinding

class RecipeFragment : Fragment() {
    private var _binding: RecipeFragmentBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("пустой фрагмент")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecipeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
// нужно ли было с let заморачиваться вообще?
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recipe = arguments?.let {
            if (Build.VERSION.SDK_INT >= 33) {
                it.getParcelable(ARG_RECIPE, Recipe::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelable(ARG_RECIPE) as? Recipe
            }
        }
        recipe?.let {
            binding.tvId.text = it.title

        } ?: run{
            binding.tvId.text = "рецепт не найдет"
        }

    }

}


