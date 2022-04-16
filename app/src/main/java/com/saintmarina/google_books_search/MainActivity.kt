package com.saintmarina.google_books_search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.saintmarina.google_books_search.booksList.BooksListActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.lang.RuntimeException

const val ITEMS_PER_PAGE = 2
class MainActivity : AppCompatActivity() {
    private val coroutine = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val input = findViewById<EditText>(R.id.input)
        val button = findViewById<Button>(R.id.lookup)
        val api = GoogleBooksAPI()

        button.setOnClickListener {
            coroutine.launch {
                try {
                    val search = "cats"//input.text.toString()
                    val rootBooks = api.getBooks("cats", 0, ITEMS_PER_PAGE)
                    Log.i("Search", rootBooks.items.toString())
                    val intent: Intent = Intent(this@MainActivity, BooksListActivity::class.java)
                        .apply {
                            putExtra("rootBooks", rootBooks)
                            putExtra("search", search)
                        }
                    startActivity(intent)
                } catch (e: RuntimeException) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}