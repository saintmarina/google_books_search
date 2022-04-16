package com.saintmarina.google_books_search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//https://www.googleapis.com/books/v1/volumes?q=cats (query term returns first 40 results)
//https://www.googleapis.com/books/v1/volumes?q=cats&startIndex=21&maxResults=20 q="cats" (query term) startIndex = 21 (start index) maxResults = 20 (number of results returned)


const val API_URL: String = "https://www.googleapis.com"

class GoogleBooksAPI {
    @Parcelize
    data class RootBooks (
        val totalItems: Int,
        val items: List<Book>?,
        ): Parcelable

    @Parcelize
    data class Book(
        val volumeInfo: VolumeInfo,
        val accessInfo: AccessInfo,
    ): Parcelable

    @Parcelize
    data class VolumeInfo(
        val title: String,
        val description: String?,
        val authors: List<String>?,
        val publishedDate: String?,
        val imageLinks: Images?,
    ): Parcelable

    @Parcelize
    data class Images(
        val smallThumbnail: String,
    ): Parcelable

    @Parcelize
    data class AccessInfo(
        val webReaderLink: String,
    ): Parcelable

    private val api = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .run { create(GoogleBooksMap::class.java) }

    interface GoogleBooksMap {
        @GET("/books/v1/volumes")
        suspend fun bookSearch(
            @Query("q") book: String,
            @Query("startIndex") startIndex: Int,
            @Query("maxResults") maxResults: Int,
        ): Response<RootBooks>
    }

    suspend fun getBooks(book: String, startIndex: Int, maxResults: Int): RootBooks {
        val response = api.bookSearch(book, startIndex, maxResults)
        if (response.isSuccessful) {
            return api.bookSearch(book, startIndex, maxResults).body()!!
        } else {
            val error = kotlin.runCatching { response.errorBody()?.string() }
            throw RuntimeException("Error: ${response.code()}. ${error}.")
        }
    }
}