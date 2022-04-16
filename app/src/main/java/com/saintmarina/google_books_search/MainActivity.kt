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

const val ITEMS_PER_PAGE = 20
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
            // First we validate the search query for the book query
            // We could also disable the lookup button if the input text is empty.
            val search = input.text.toString().trim()
            if (search.isEmpty()) {
                Toast.makeText(this@MainActivity, "Missing search query", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            coroutine.launch {
                // Then we fetch the results. If it fails, we display an error message
                val rootBooks = try {
                    api.getBooks(search, 0, ITEMS_PER_PAGE)
                } catch (e: RuntimeException) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                    return@launch
                }

                // Perhaps the search query didn't return anything. Bail.
                if (rootBooks.items == null) {
                    Toast.makeText(this@MainActivity, "No results", Toast.LENGTH_LONG).show()
                    return@launch
                }

                // We have something to display, show it.
                val intent: Intent =
                    Intent(this@MainActivity, BooksListActivity::class.java)
                        .apply {
                            putExtra("rootBooks", rootBooks)
                            putExtra("search", search)
                        }
                startActivity(intent)
            }
        }
    }
}