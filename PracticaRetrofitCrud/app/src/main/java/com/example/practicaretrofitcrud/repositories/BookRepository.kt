package com.example.practicaretrofitcrud.repositories

import com.example.practicaretrofitcrud.api.LibraryService
import com.example.practicaretrofitcrud.api.dto.Book
import com.example.practicaretrofitcrud.api.dto.Books

object BookRepository {
    suspend fun getBooksList(): Books {
        return RetrofitRepository
            .getRetrofitInstance()
            .create(LibraryService::class.java)
            .getBooks()
    }

    suspend fun getBookById(id: Long): Book {
        return RetrofitRepository
            .getRetrofitInstance()
            .create(LibraryService::class.java)
            .getBookById(id)
    }

    suspend fun saveBook(book: Book): Book {
        return if (book.id == 0L) {
            insertBook(book)
        } else {
            updateBook(book)
        }
    }

    private suspend fun updateBook(book: Book): Book {
        return RetrofitRepository
            .getRetrofitInstance()
            .create(LibraryService::class.java)
            .updateBook(book.id, book)
    }

    private suspend fun insertBook(book: Book): Book {
        return RetrofitRepository
            .getRetrofitInstance()
            .create(LibraryService::class.java)
            .insertBook(book)
    }

    suspend fun deleteBook(book: Book) {
        RetrofitRepository
            .getRetrofitInstance()
            .create(LibraryService::class.java)
            .deleteBook(book.id)
    }
}