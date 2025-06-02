package com.example.recipesapp.ui.recipe.recipe

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.icu.text.IDNA.Info
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.model.Recipe
import com.example.recipesapp.R
import com.example.recipesapp.ui.recipe.recipesList.RecipesListFragment.Companion.ARG_RECIPE
import com.example.recipesapp.databinding.RecipeFragmentBinding
import com.example.recipesapp.ui.IngredientsAdapter
import com.example.recipesapp.ui.MethodAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {
    private var _binding: RecipeFragmentBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding не инициализирован")
    private val viewModel: RecipeViewModel by viewModels()


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

        val recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ARG_RECIPE) as? Recipe
        }


        if (recipe != null) {
            initUI(recipe)

        } else {
            binding.tvRecipeName.text = "Рецепт не найден"
        }

    }


    private fun initUI(recipe: Recipe) {
        val methodAdapter = MethodAdapter(emptyList())
        val ingredientsAdapter = IngredientsAdapter(emptyList())
        with(binding.rvMethod) {
            adapter = methodAdapter
            addMaterialDivider(this)
        }
        with(binding.rvIngredients) {
            adapter = ingredientsAdapter
            addMaterialDivider(this)
        }
        binding.sbRecipeSeek.setOnSeekBarChangeListener(PortionSeekBarListener { progress ->
            viewModel.onChangePortions(
                progress
            )
        })


        binding.ibFavorite.setOnClickListener {
            viewModel.onFavoriteClicked(recipe?.id!!.toInt())
        }
        viewModel.loadRecipe(recipe.id)
        viewModel.recipeState.observe(viewLifecycleOwner, Observer { state ->
            IngredientsAdapter(state.recipe?.ingredients ?: emptyList())



            MethodAdapter(state.recipe?.method ?: emptyList())



            ingredientsAdapter.updateIngredients(state.portionsCount)

            Log.i("!!!", "State changed, isFavorite: ${state.isFavorite}")
            with(binding) {
                ivRecipe.setImageDrawable(state.recipeImage)
                tvRecipeName.text = state.recipe?.title
                if (state.isFavorite) {
                    ibFavorite.setImageResource(R.drawable.ic_heart)
                } else {
                    ibFavorite.setImageResource(R.drawable.ic_heart_empty)
                }
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

    class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) : OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            onChangeIngredients(progress)
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {

        }

        override fun onStopTrackingTouch(p0: SeekBar?) {

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}