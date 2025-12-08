package com.example.recipesapp.ui.recipe.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.RecipesApplication
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.ui.recipe.recipesList.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("пустой фрагмент")
    private lateinit var viewModel: FavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        viewModel = appContainer.favoritesViewModelFactory.create()
    }

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
        val action = FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(recipeId)
        findNavController().navigate(action)


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}