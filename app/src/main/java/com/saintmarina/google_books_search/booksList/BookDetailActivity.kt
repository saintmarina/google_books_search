package com.saintmarina.google_books_search.booksList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.saintmarina.google_books_search.GoogleBooksAPI
import com.saintmarina.google_books_search.R

class BookDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)
        val book = this.intent.getParcelableExtra<GoogleBooksAPI.Book>("book")!!
        val titleText = findViewById<TextView>(R.id.title_text)
        val descriptionText = findViewById<TextView>(R.id.description_text)
        val authorsText = findViewById<TextView>(R.id.authors_text)
        val dateText = findViewById<TextView>(R.id.date_text)

        titleText.text = book.volumeInfo.title
        descriptionText.text = book.volumeInfo.description ?: "No description provided"
        authorsText.text = if (book.volumeInfo.authors != null)  book.volumeInfo.authors.joinToString( ", ") else  "No description provided"
        dateText.text = book.volumeInfo.publishedDate
    }
}