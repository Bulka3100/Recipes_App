package com.example.recipesapp

import android.content.Context
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

    // Lazy-инициализация зачем тут? Она всегда для shared?
    private val sharedPrefs by lazy {
        requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
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
            val favorites = getFavorites()
            var isFavorite = recipe.id.toString() in favorites
            ibFavorite.setImageResource(if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty)

            ibFavorite.setOnClickListener {
                val updatedFavorites = getFavorites()
                isFavorite = !isFavorite
                if (isFavorite) {
                    updatedFavorites.add(recipe.id.toString())
                } else {
                    updatedFavorites.remove(recipe.id.toString())
                }
                saveFavorites(updatedFavorites)
                ibFavorite.setImageResource(if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty)
            }
        }
    }

    private fun initRecycler(recipe: Recipe) {
        val ingredientsAdapter = IngredientsAdapter(recipe.ingredients)
        binding.rvIngredients.apply {
            adapter = ingredientsAdapter
            addMaterialDivider(this)
        }

        with(binding.rvMethod) {
            adapter = MethodAdapter(recipe.method)
            addMaterialDivider(this)
        }

        binding.sbRecipeSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                ingredientsAdapter.updateIngredients(progress)
                binding.tvPortionsCount.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun saveFavorites(set: Set<String>) {
        val editor = sharedPrefs.edit()
        editor.putStringSet(KEY_FAVORITES, set)
        editor.apply()
    }

    private fun getFavorites(): MutableSet<String> {
        val favoritesId = sharedPrefs.getStringSet(KEY_FAVORITES, emptySet())
        return HashSet(favoritesId ?: emptySet())
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val PREFS_NAME = "recipePrefs"
        const val KEY_FAVORITES = "favorite_recipe_id"
    }

    // Расширение для dp
    private val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}