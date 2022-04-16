package com.saintmarina.google_books_search.booksList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.saintmarina.google_books_search.GoogleBooksAPI.Book
import com.saintmarina.google_books_search.R

class BookListAdapter(context: Context, private val resource: Int, private val onClick: (Book) -> Unit) :
    ArrayAdapter<Book>(context, resource) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val book = this.getItem(position)!!
        val item = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        return item.apply {
            setOnClickListener { onClick(book) }
            Glide.with(context)
                .load(book.volumeInfo.imageLinks.smallThumbnail)
                .placeholder(R.drawable.ic_book_placeholder)
                .into(findViewById(R.id.thumbnail))
            findViewById<TextView>(R.id.book_text).text = book.volumeInfo.title

        }
    }
}