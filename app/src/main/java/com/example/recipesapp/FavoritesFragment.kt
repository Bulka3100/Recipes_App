package com.example.recipesapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipesapp.RecipeFragment.Companion
import com.example.recipesapp.RecipesListFragment.Companion.ARG_RECIPE
import com.example.recipesapp.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("пустой фрагмент")
    private val sharedPrefs by lazy {
        requireContext().getSharedPreferences(RecipeFragment.PREFS_NAME,
            Context.MODE_PRIVATE)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
initRecycler()
    }
    private fun initRecycler(){
        val favorites = getFavorites()
        val favoriteIds = favorites.mapNotNull { it.toInt() }.toSet()
       val favoriteRecipes = STUB.getRecipesbyIds(favoriteIds)
        val adapter = RecipesListAdapter(favoriteRecipes)
        adapter.setOnItemClickListener(
            object :RecipesListAdapter.OnItemClickListener{
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            }
        )
    }

    private fun getFavorites():MutableSet<String>{
        val getId = sharedPrefs.getStringSet(RecipeFragment.KEY_FAVORITES, emptySet<String>())
        return HashSet(getId ?: emptySet())
    }
    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer)
            addToBackStack(null)
            setReorderingAllowed(true)
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}