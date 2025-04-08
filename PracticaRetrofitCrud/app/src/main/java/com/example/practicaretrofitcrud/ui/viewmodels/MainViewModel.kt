package com.example.practicaretrofitcrud.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaretrofitcrud.api.dto.Book
import com.example.practicaretrofitcrud.api.dto.Books
import com.example.practicaretrofitcrud.repositories.BookRepository
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _bookList: MutableLiveData<List<Book>> =
        MutableLiveData(mutableListOf())
    val bookList: LiveData<List<Book>> = _bookList

    private val _bookDeleted: MutableLiveData<Book> = MutableLiveData(null)
    val bookDeleted: LiveData<Book> = _bookDeleted

    fun getBookList() {
        viewModelScope.launch {
            _bookList.postValue(BookRepository.getBooksList())
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            Log.d("Book", "Deleting book with id ${book.id}")
            BookRepository.deleteBook(book)
            _bookDeleted.postValue(book)
        }
    }
}