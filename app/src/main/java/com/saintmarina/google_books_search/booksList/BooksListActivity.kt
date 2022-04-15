package com.saintmarina.google_books_search.booksList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.saintmarina.google_books_search.GoogleBooksAPI
import com.saintmarina.google_books_search.R

class BooksListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books_list)
        val rootBooks = this.intent.getParcelableExtra<GoogleBooksAPI.RootBooks>("rootBooks")!!
        val listView = findViewById<ListView>(R.id.books_list_view)

        val onClick = { book: GoogleBooksAPI.Book ->
            val intent: Intent = Intent(this, BookDetailActivity::class.java)
                .apply {
                    putExtra("book", book)
                }
            startActivity(intent)
        }
        listView.adapter = BookListAdapter(this, R.layout.book_list_item, onClick)
            .apply { addAll(rootBooks.items) }
    }
}