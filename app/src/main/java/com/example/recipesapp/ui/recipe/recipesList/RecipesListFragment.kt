package com.example.recipesapp.ui.recipe.recipesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.recipesapp.R
import com.example.recipesapp.data.STUB
import com.example.recipesapp.databinding.FragmentRecipesBinding
import com.example.recipesapp.ui.categories.CategoriesListFragment
import com.example.recipesapp.ui.recipe.recipe.RecipeFragment

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding must not be null")
    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null
    private val viewModel: RecipesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initRecycler()
        viewModel.loadRecipes(categoryId!!)
    }

    private fun initUi() {

        arguments?.let {
            categoryId = it.getInt(CategoriesListFragment.ARG_CATEGORY_ID)
            categoryName = it.getString(CategoriesListFragment.ARG_CATEGORY_NAME)
            categoryImageUrl = it.getString(CategoriesListFragment.ARG_CATEGORY_IMAGE_URL)
            binding.tvRecipeNaming.text = categoryName
            val inputStream =
                requireContext().assets.open(
                    categoryImageUrl ?: throw IllegalStateException("null")
                )
            binding.ivRecipeCategory.setImageDrawable(
                android.graphics.drawable.Drawable.createFromStream(
                    inputStream,
                    null
                )
            )
        }
    }

    private fun initRecycler() {
        val adapter: RecipesListAdapter = RecipesListAdapter(emptyList())
        binding.rvRecipes.adapter = adapter

        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            adapter.updateData(recipes)
        }

        adapter.setOnItemClickListener(
            object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }

            }
        )

    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = viewModel.getRecipeById(recipeId)
        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundleOf(ARG_RECIPE to recipe))
            addToBackStack(null)
            setReorderingAllowed(true)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_RECIPE = "arg_recipe"
    }
}