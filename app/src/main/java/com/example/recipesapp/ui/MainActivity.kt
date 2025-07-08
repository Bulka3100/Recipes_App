package com.example.recipesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ActivityMainBinding
import com.example.recipesapp.model.Category
import com.example.recipesapp.ui.categories.CategoriesListFragment
import com.example.recipesapp.ui.recipe.favorites.FavoritesFragment
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("binding must not be null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Log.i("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")
        val thread = Thread {
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val json = connection.inputStream.bufferedReader().readText()
            Log.i("!!!", "Выполняю запрос на потоке : ${Thread.currentThread().name}")
            Log.i("!!!", "responseCode: ${connection.responseCode}")
            Log.i("!!!", "responseMessage: ${connection.responseCode}")
            Log.i("!!!", "body: ${json}")
            println(connection.inputStream.bufferedReader().readText())
            val deserealized = Json.decodeFromString<List<Category>>(json)
        }
        thread.start()




        binding.btnFavorites.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.favoritesFragment)

        }

        binding.btnCategories.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.categoriesListFragment)


        }
    }
}



