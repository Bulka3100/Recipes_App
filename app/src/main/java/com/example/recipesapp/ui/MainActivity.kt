package com.example.recipesapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ActivityMainBinding
import com.example.recipesapp.ui.category.CategoriesListFragment
import com.example.recipesapp.ui.recipe.favorites.FavoritesFragment

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("binding must not be null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)




        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.mainContainer, CategoriesListFragment())
            }
        }

        binding.btnFavorites.setOnClickListener {

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.mainContainer, FavoritesFragment())
            }
        }

            binding.btnCategories.setOnClickListener {

                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.mainContainer, CategoriesListFragment ())
                }
            }
        }
    }



