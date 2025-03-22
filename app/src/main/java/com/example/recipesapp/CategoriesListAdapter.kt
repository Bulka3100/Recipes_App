package com.example.recipesapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.Data_Classes.Category
import com.example.recipesapp.databinding.ItemCategoryBinding
import java.io.InputStream

// Адаптер для RecyclerView, который отображает список категорий.
// Принимает список объектов Category как входные данные.
class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    // Интерфейс для обработки кликов по элементам списка.
    // Определяет контракт: любой класс, реализующий этот интерфейс, должен иметь метод onItemClick.
    interface OnItemClickListener {
        fun onItemClick() // Передает объект Category при клике
    }

    // Переменная для хранения слушателя кликов.
    // Изначально null, будет установлена через setOnItemClickListener.
    private var itemClickListener: OnItemClickListener? = null

    // Метод для установки слушателя кликов.
    // Позволяет внешнему коду (например, фрагменту) передать свою реализацию OnItemClickListener.

        //мы сами прописываем listener и внутри него реализацию функции onItemClick
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    // Внутренний класс ViewHolder, который хранит ссылки на UI-элементы одной карточки.
    // Наследуется от RecyclerView.ViewHolder и принимает binding.root как корневой View.
    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        // Метод bind связывает данные категории с UI-элементами карточки.
        fun bind(category: Category, context: Context) {
            // Устанавливаем текст для названия и описания
            binding.tvNameCt.text = category.title
            binding.tvDescription.text = category.description

            // Загружаем изображение из assets
            try {
                val imagePath = category.imageUrl // Путь к изображению из объекта Category
                val inputStream: InputStream = context.assets.open(imagePath) // Открываем поток
                val drawable = Drawable.createFromStream(inputStream, null) // Создаем Drawable

                if (drawable == null) {
                    // Если изображение не загрузилось, можно добавить заглушку (placeholder)
                } else {
                    binding.ivCategories.setImageDrawable(drawable) // Устанавливаем изображение
                }
                inputStream.close() // Закрываем поток
            } catch (e: Exception) {
                // Обработка ошибок (рекомендуется добавить логирование)
            }
        }
    }

    // Создает новый ViewHolder, когда RecyclerView нужно отобразить новый элемент.
    // Вызывается только для создания ограниченного числа ViewHolder'ов (переиспользуемых).
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Создаем binding из XML-файла разметки (item_category.xml)
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding) // Возвращаем новый ViewHolder с привязанным binding
    }

    // Связывает данные с уже созданным ViewHolder'ом для конкретной позиции.
    // Вызывается при прокрутке списка, когда ViewHolder переиспользуется.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = dataSet[position] // Получаем объект Category для текущей позиции
        holder.bind(category, holder.itemView.context) // Привязываем данные к UI

        // Устанавливаем слушатель кликов на корневой элемент карточки
        holder.binding.root.setOnClickListener {
            itemClickListener?.onItemClick() // Вызываем метод интерфейса при клике
        }
    }

    // Возвращает общее количество элементов в списке.
    // RecyclerView использует это, чтобы знать, сколько элементов отображать.
    override fun getItemCount(): Int = dataSet.size
}