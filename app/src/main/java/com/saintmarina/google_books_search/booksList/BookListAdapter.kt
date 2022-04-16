package com.saintmarina.google_books_search.booksList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.saintmarina.google_books_search.GoogleBooksAPI.Book
import com.saintmarina.google_books_search.R

class BookListAdapter(context: Context, private val resource: Int, private val onClick: (Book) -> Unit) :
    ArrayAdapter<Book>(context, resource) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Not sure how to use binding here. Because we want to reuse convertView
        val book = this.getItem(position)!!

        return (convertView ?: LayoutInflater.from(context).inflate(resource, parent, false))
            .apply {
                setOnClickListener { onClick(book) }

                // If book thumbnail provided set it, otherwise set a placeholder
                if (book.volumeInfo.imageLinks != null) {
                    Glide.with(context)
                        .load(book.volumeInfo.imageLinks.smallThumbnail)
                        .placeholder(R.drawable.ic_book_placeholder)
                        .into(findViewById(R.id.thumbnail))
                } else {
                    findViewById<ImageView>(R.id.thumbnail).setImageResource(R.drawable.ic_book_placeholder)
                }
                findViewById<TextView>(R.id.book_title).text = book.volumeInfo.title
                findViewById<TextView>(R.id.book_authors).text = book.volumeInfo.authors?.joinToString(", ")
                findViewById<TextView>(R.id.book_publish_date).text = book.volumeInfo.publishedDate
            }
    }
}