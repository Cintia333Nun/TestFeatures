package com.cin.testfeatures.retrofit_mvvm.repository

import com.cin.testfeatures.retrofit_mvvm.datasourse.BooksDataSource
import com.cin.testfeatures.retrofit_mvvm.service.BooksApi

class BooksRepository(private val booksApi: BooksApi) {
    fun requestBooksDataSource(): BooksDataSource = BooksDataSource(booksApi)
}