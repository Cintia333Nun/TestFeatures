package com.cin.testfeatures.retrofit_mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.cin.testfeatures.retrofit_mvvm.repository.BooksRepository

class BooksViewModel(private val booksRepository: BooksRepository): ViewModel() {
    private val booksDataSource = booksRepository.requestBooksDataSource()

    val responseVolume = booksDataSource.responseVolume
    val networkState = booksDataSource.networkState

    fun getBooks(query: String, author: String) {
        booksDataSource.getBooks(query, author)
    }
}