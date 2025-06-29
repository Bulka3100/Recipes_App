package com.example.recipesapp.ui.recipe.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.recipesapp.KEY_FAVORITES
import com.example.recipesapp.PREFS_NAME
import com.example.recipesapp.R
import com.example.recipesapp.ui.recipe.recipesList.RecipesListFragment.Companion.ARG_RECIPE
import com.example.recipesapp.data.STUB
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.ui.recipe.recipe.RecipeFragment
import com.example.recipesapp.ui.recipe.recipesList.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("пустой фрагмент")
    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {

        val adapter = RecipesListAdapter(emptyList())
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            adapter.upgradeData(state.recipes)
            binding.tvNoFavorites.isVisible = state.recipes.isEmpty()

        }

        binding.rvFavoriteRecipes.adapter = adapter
        adapter.setOnItemClickListener(
            object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            }
        )
        viewModel.loadFavorites()
    }


    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = viewModel.getRecipe(recipeId)
        if (recipe != null) {
            parentFragmentManager.commit {
                replace<RecipeFragment>(R.id.mainContainer, args = bundleOf(ARG_RECIPE to recipe))
                addToBackStack(null)
                setReorderingAllowed(true)
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}