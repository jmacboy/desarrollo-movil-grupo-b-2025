package com.example.practicaretrofitcrud.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaretrofitcrud.api.dto.Book
import com.example.practicaretrofitcrud.repositories.BookRepository
import kotlinx.coroutines.launch

class BookDetailViewModel : ViewModel() {
    private val _book: MutableLiveData<Book> = MutableLiveData(null)
    val book: LiveData<Book> = _book


    private val _hasErrorSaving: MutableLiveData<Boolean> = MutableLiveData(false)
    val hasErrorSaving: LiveData<Boolean> = _hasErrorSaving

    private val _bookSaved: MutableLiveData<Book> = MutableLiveData(null)
    val bookSaved: LiveData<Book> = _bookSaved

    fun loadBook(id: Long) {
        viewModelScope.launch {
            val book = BookRepository.getBookById(id)
            _book.postValue(book)
        }
    }

    fun saveBook(book: Book) {
        viewModelScope.launch {
            try {
                _bookSaved.postValue(BookRepository.saveBook(book))
            } catch (e: Exception) {
                e.printStackTrace()
                _hasErrorSaving.postValue(true)
            }
        }
    }


}