package com.example.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.databinding.FragmentListCategoriesBinding
import com.example.recipesapp.databinding.FragmentRecipesBinding

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding must not be null")
    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null
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
            initRecycler()
        }
    }

    private fun initRecycler() {
        val recipes = STUB.getRecipesByCategoryId(0)
        val adapter = RecipesListAdapter(recipes)

        adapter.setOnItemClickListener(
            object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }

            }
        )
        binding.rvRecipes.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context)


        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        parentFragmentManager.commit {
            replace(R.id.mainContainer, RecipeFragment())
            addToBackStack(null)
            setReorderingAllowed(true)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}