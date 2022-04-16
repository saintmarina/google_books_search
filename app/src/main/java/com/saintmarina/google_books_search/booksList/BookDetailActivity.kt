package com.saintmarina.google_books_search.booksList

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.saintmarina.google_books_search.GoogleBooksAPI
import com.saintmarina.google_books_search.R
import com.saintmarina.google_books_search.WebViewActivity
import com.saintmarina.google_books_search.databinding.ActivityBookDetailBinding

class BookDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val book = this.intent.getParcelableExtra<GoogleBooksAPI.Book>("book")!!
        title = book.volumeInfo.title.replaceFirstChar(Char::titlecase)

        binding.detailTitleText.text = book.volumeInfo.title
        binding.detailAuthorsText.text = book.volumeInfo.authors?.joinToString( ", ") ?: "Unknown author"
        binding.detailDateText.text = book.volumeInfo.publishedDate

        // If book thumbnail provided set it, otherwise set a placeholder
        if (book.volumeInfo.imageLinks != null) {
            Glide.with(this)
                .load(book.volumeInfo.imageLinks.smallThumbnail)
                .placeholder(R.drawable.ic_book_placeholder)
                .into(binding.detailThumbnail)
        } else {
            binding.detailThumbnail.setImageResource(R.drawable.ic_book_placeholder)
        }

        binding.detailDescriptionText.text = book.volumeInfo.description ?: "No description provided"

        binding.readBookBtn.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
                .apply {
                    putExtra("url", book.accessInfo.webReaderLink)
                    putExtra("title", book.volumeInfo.title)
                }
            startActivity(intent)

        }
    }
}