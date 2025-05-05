package com.example.recipesapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipesapp.R
import com.example.recipesapp.data.STUB
import com.example.recipesapp.databinding.FragmentListCategoriesBinding
import com.example.recipesapp.ui.recipe.recipesList.RecipesListFragment

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding must not be null")

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
        initRecycler()
    }

    private fun initRecycler() {
        val dataSet = STUB.getCategories()

        val categoriesAdapter = CategoriesListAdapter(dataSet)
        categoriesAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
        //почему требуется this
        binding.rvCategorie.apply {
            this.adapter = categoriesAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val category = STUB.getCategories().find { it.id == categoryId }
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
//    или может лучше использовать findNavController().navigate(R.id.action_categoriesListFragment_to_recipesListFragment) ? Не до конца понял суть, но могу разобраться если это лучший вариант

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //правильно понял что надо было так сделать?
    companion object {
        const val ARG_CATEGORY_ID = "category_id"
        const val ARG_CATEGORY_NAME = "category_name"
        const val ARG_CATEGORY_IMAGE_URL = "category_image"
    }

}
