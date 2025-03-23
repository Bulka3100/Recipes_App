package com.example.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
        }
    }
}