package com.example.recipesapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.example.recipesapp.BASE_URL
import com.example.recipesapp.RecipesApplication
import com.example.recipesapp.data.repository.AppDataBase
import com.example.recipesapp.data.repository.RecipesApiService
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.databinding.FragmentListCategoriesBinding
import com.example.recipesapp.di.AppContainer
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding must not be null")
    private lateinit var viewModel: CategoriesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        viewModel = appContainer.categoriesListViewModelFactory.create()
    }

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
            ?: throw IllegalStateException("category not found")
        val action =
            CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                category
            )
        findNavController().navigate(action)
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
