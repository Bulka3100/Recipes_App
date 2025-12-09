package com.example.recipesapp.ui.recipe.recipe

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.RecipesApplication
import com.example.recipesapp.databinding.RecipeFragmentBinding
import com.example.recipesapp.ui.IngredientsAdapter
import com.example.recipesapp.ui.MethodAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {
    private var _binding: RecipeFragmentBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding не инициализирован")
    private lateinit var viewModel: RecipeViewModel
    private val args: RecipeFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        viewModel = appContainer.recipeViewModelFactory.create()
    }

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

        initUI()

        val recipeId = args.recipeId
        if (recipeId != null) {
            viewModel.loadRecipe(recipeId)
        } else {
            binding.tvRecipeName.text = "ID рецепта не найден"
        }
    }

    private fun initUI() {
        val methodAdapter = MethodAdapter(emptyList())
        val ingredientsAdapter = IngredientsAdapter(emptyList())

        with(binding.rvMethod) {
            adapter = methodAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addMaterialDivider(this)
        }
        with(binding.rvIngredients) {
            adapter = ingredientsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addMaterialDivider(this)
        }

        binding.sbRecipeSeek.setOnSeekBarChangeListener(PortionSeekBarListener { progress ->
            viewModel.onChangePortions(progress)
        })

        binding.ibFavorite.setOnClickListener {
            args.recipeId?.let { recipeId ->
                viewModel.onFavoriteClicked(recipeId)
            }
        }

        viewModel.recipeState.observe(viewLifecycleOwner, Observer { state ->
            ingredientsAdapter.dataSet = state.recipe?.ingredients ?: emptyList()
            methodAdapter.dataSet = state.recipe?.method ?: emptyList()

            ingredientsAdapter.updateIngredients(state.portionsCount)

            with(binding) {
                val imageUrl = state.recipeImageUrl

                Glide.with(requireContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_error)
                    .into(ivRecipe)

                tvRecipeName.text = state.recipe?.title ?: "Загрузка..."

                ibFavorite.setImageResource(
                    if (state.isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty
                )
            }
        })
    }

    private fun addMaterialDivider(rv: RecyclerView) {
        rv.addItemDecoration(
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                dividerThickness = 1.dp
                isLastItemDecorated = false
                dividerColor = Color.parseColor("#F5F5F5")
                dividerInsetStart = 12.dp
                dividerInsetEnd = 12.dp
            }
        )
    }

    class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) :
        SeekBar.OnSeekBarChangeListener {

        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            onChangeIngredients(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}