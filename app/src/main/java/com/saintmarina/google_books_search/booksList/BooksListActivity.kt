package com.saintmarina.google_books_search.booksList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
        title = search.replaceFirstChar(Char::titlecase)

        val onClick = { book: GoogleBooksAPI.Book ->
            val intent: Intent = Intent(this, BookDetailActivity::class.java)
                .apply {
                    putExtra("book", book)
                }
            startActivity(intent)
        }

        val bookListAdapter = BookListAdapter(this, R.layout.book_list_item, onClick)
            .apply { addAll(rootBooks.items!!) } // it cannot be null, it's checked in the main activity
        listView.adapter = bookListAdapter


        if (bookListAdapter.count < rootBooks.totalItems) {
            val loadMoreBtn = Button(this).apply { text = "Load More" }
            listView.addFooterView(loadMoreBtn)
            loadMoreBtn.setOnClickListener {
                coroutine.launch {
                    val additionalBooks = try {
                        api.getBooks(search, bookListAdapter.count, ITEMS_PER_PAGE)
                    } catch (e: RuntimeException) {
                        Toast.makeText(this@BooksListActivity, e.message, Toast.LENGTH_LONG).show()
                        return@launch
                    }
                    // If we don't find additional books, we remove the load more button.
                    // Using 'bookListAdapter.count >= rootBooks.totalItems' doesn't work reliably.
                    if (additionalBooks.items == null) {
                        Toast.makeText(this@BooksListActivity, "No more books to load", Toast.LENGTH_LONG).show()
                        listView.removeFooterView(loadMoreBtn)
                    } else {
                        bookListAdapter.addAll(additionalBooks.items)
                    }
                }
            }
        }
    }
}