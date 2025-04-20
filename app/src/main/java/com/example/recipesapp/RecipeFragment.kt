package com.example.recipesapp

import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.Data_Classes.Recipe
import com.example.recipesapp.RecipesListFragment.Companion.ARG_RECIPE
import com.example.recipesapp.databinding.RecipeFragmentBinding
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {
    private var _binding: RecipeFragmentBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding не инициализирован")

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
            initRecycler(recipe)
        } else {
            binding.tvRecipeName.text = "Рецепт не найден"
        }
    }

    private fun initUI(recipe: Recipe) {
        with(binding) {
            tvRecipeName.text = recipe.title
            ibFavorite.setImageResource(R.drawable.ic_heart_empty)
            ibFavorite.setOnClickListener{
                ibFavorite.setImageResource(R.drawable.ic_heart)
            }

        }
    }

    private fun initRecycler(recipe: Recipe) {
        binding.rvIngredients.apply {
            adapter = IngredientsAdapter(recipe.ingredients)
            addMaterialDivider(this)


        }


        with(binding.rvMethod) {
            adapter = MethodAdapter(recipe.method)
            addMaterialDivider(this)
        }
        binding.sbRecipeSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                    вот эта часть мне не нравится я же тут по сути пересоздаю адаптер заново, плохая практика. А это callback кстати? запутался
                IngredientsAdapter(recipe.ingredients).updateIngredients(progress)
                //нормально тут изменять количество ингредиентов?
                binding.tvPortionsCount.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    // Расширение для dp
    private val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}