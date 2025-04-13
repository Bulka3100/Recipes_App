package com.example.recipesapp

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.Data_Classes.Recipe
import com.example.recipesapp.RecipesListFragment.Companion.ARG_RECIPE
import com.example.recipesapp.databinding.RecipeFragmentBinding
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {
    private var _binding: RecipeFragmentBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding не инициализирован")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecipeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ARG_RECIPE) as? Recipe
        }

        if (recipe != null) {
            initUI(recipe)
            initRecycler(recipe)
        } else {
            binding.tvRecipeName.text = "Рецепт не найден"
        }
    }

    private fun initUI(recipe: Recipe) {
        with(binding) {
            tvRecipeName.text = recipe.title

        }
    }

    private fun initRecycler(recipe: Recipe) {
        with(binding.rvIngredients) {
            adapter = IngredientsAdapter(recipe.ingredients)
            addItemDecoration(
                MaterialDividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
                    dividerThickness = 1.dp
                    isLastItemDecorated = false
                }
            )
        }

        with(binding.rvMethod) {
            adapter = MethodAdapter(recipe.method)
            addItemDecoration(
                MaterialDividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
                    dividerThickness = 1.dp
                    isLastItemDecorated = false
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Расширение для dp
    private val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}