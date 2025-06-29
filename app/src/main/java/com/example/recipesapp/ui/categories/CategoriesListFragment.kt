package com.example.recipesapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipesapp.R
import com.example.recipesapp.data.STUB
import com.example.recipesapp.databinding.FragmentListCategoriesBinding
import com.example.recipesapp.ui.recipe.recipesList.RecipesListFragment

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding must not be null")
    private val viewModel: CategoriesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadCategories()
        initRecycler()
    }

    private fun initRecycler() {
        val categoriesAdapter = CategoriesListAdapter(emptyList())
        viewModel.categoryState.observe(viewLifecycleOwner) { state ->
            categoriesAdapter.updateData(state.categoriesList)

        }

        categoriesAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
        binding.rvCategorie.apply {
            this.adapter = categoriesAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val category = viewModel.categoryState.value?.categoriesList?.find { it.id == categoryId }
        val categoryName: String = category?.title ?: "Unknown"
        val categoryImageUrl: String = category?.imageUrl ?: "Unknown"

        val bundle = bundleOf(
            ARG_CATEGORY_ID to categoryId,
            ARG_CATEGORY_NAME to categoryName,
            ARG_CATEGORY_IMAGE_URL to categoryImageUrl
        )
        val fragment = RecipesListFragment().apply {
            arguments = bundle
        }
        parentFragmentManager.commit {
            replace(R.id.mainContainer, fragment)
            addToBackStack(null)
            setReorderingAllowed(true)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        const val ARG_CATEGORY_ID = "category_id"
        const val ARG_CATEGORY_NAME = "category_name"
        const val ARG_CATEGORY_IMAGE_URL = "category_image"
    }

}
