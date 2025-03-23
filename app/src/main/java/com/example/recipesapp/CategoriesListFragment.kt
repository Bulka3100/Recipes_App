package com.example.recipesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.databinding.FragmentListCategoriesBinding

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

        val adapter = CategoriesListAdapter(dataSet)
        adapter.setOnItemClickListener(object : CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick() {
                openRecipesByCategoryId()
            }
        })

        binding.rvCategorie.apply {
            this.adapter = adapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun openRecipesByCategoryId() {

        parentFragmentManager // Плчему лушче спользуовать parentFragmentManager вместо supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainContainer, RecipesListFragment())
            .addToBackStack(null) //нужно ли?
            .commit()
    }
//    или может лучше использовать findNavController().navigate(R.id.action_categoriesListFragment_to_recipesListFragment) ? Не до конца понял суть, но могу разобраться если это лучший вариант

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
