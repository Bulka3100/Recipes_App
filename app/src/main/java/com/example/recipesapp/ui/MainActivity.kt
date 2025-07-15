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
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("binding must not be null")
    val threadPool = Executors.newFixedThreadPool(10)
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val client = OkHttpClient.Builder().addInterceptor(logging).build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Log.i("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")


        threadPool.execute {

            val request =
                Request.Builder().url("https://recipes.androidsprint.ru/api/category").build()

            client.newCall(request).execute().use { response ->
                val json = response.body?.string() ?: ""
                val deserealized = Json.decodeFromString<List<Category>>(json)
                Log.i("!!!", "Выполняю запрос на потоке : ${Thread.currentThread().name}")
                Log.i("!!!", "responseCode: ${response.code}")
                Log.i("!!!", "responseMessage: ${response.message}")
                Log.i("!!!", "body: ${json}")
                println(response.body)
                loadCategories(deserealized)

            }

        }



        binding.btnFavorites.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.favoritesFragment)

        }

        binding.btnCategories.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.categoriesListFragment)


        }
    }

    fun loadCategories(categories: List<Category>) {
        threadPool.execute {
            val ids = categories.map { it.id }
            for (i in ids) {
                try {

                    val request = Request.Builder()
                        .url("https://recipes.androidsprint.ru/api/category/${i}/recipes")
                        .build()
                    client.newCall(request).execute().use { response ->
                        val json = response.body.toString()
                        println(json)
                    }

                } catch (e: Exception) {
                    Log.e(
                        "!!!",
                        "Ошибка при загрузке рецептов для категории $i: ${e.message}"
                    )

                }
            }


        }

    }

}



