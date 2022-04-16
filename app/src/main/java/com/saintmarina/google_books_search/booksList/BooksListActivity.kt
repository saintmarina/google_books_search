package com.saintmarina.google_books_search.booksList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.saintmarina.google_books_search.GoogleBooksAPI
import com.saintmarina.google_books_search.ITEMS_PER_PAGE
import com.saintmarina.google_books_search.R
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.lang.RuntimeException

class BooksListActivity : AppCompatActivity() {
    private val coroutine = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books_list)
        val rootBooks = this.intent.getParcelableExtra<GoogleBooksAPI.RootBooks>("rootBooks")!!
        val search = this.intent.getStringExtra("search")!!
        val listView = findViewById<ListView>(R.id.books_list_view)
        val api = GoogleBooksAPI()

        val onClick = { book: GoogleBooksAPI.Book ->
            val intent: Intent = Intent(this, BookDetailActivity::class.java)
                .apply {
                    putExtra("book", book)
                }
            startActivity(intent)
        }

        val bookListAdapter = BookListAdapter(this, R.layout.book_list_item, onClick)
            .apply { addAll(rootBooks.items) }
        listView.adapter = bookListAdapter


        val loadMoreBtn = Button(this).apply { text = "Load More" }
        loadMoreBtn.setOnClickListener {
            coroutine.launch {
                try {
                    val additionalBooks = api.getBooks(search, bookListAdapter.count, ITEMS_PER_PAGE)
                    bookListAdapter.addAll(additionalBooks.items)
                } catch (e: RuntimeException) {
                    Toast.makeText(this@BooksListActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        listView.addFooterView(loadMoreBtn)
    }
}