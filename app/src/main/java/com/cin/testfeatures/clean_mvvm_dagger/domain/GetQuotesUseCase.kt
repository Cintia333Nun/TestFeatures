package com.cin.testfeatures.clean_mvvm_dagger.domain

import com.cin.testfeatures.clean_mvvm_dagger.data.QuoteRepository
import com.cin.testfeatures.clean_mvvm_dagger.data.data_base.entities.toDatabase
import com.cin.testfeatures.clean_mvvm_dagger.domain.model.Quote
import javax.inject.Inject

class GetQuotesUseCase @Inject constructor(private val repository: QuoteRepository) {
    suspend operator fun invoke(): List<Quote> {
        val quotes = repository.getAllQuotesFromApi()

        return if(quotes.isNotEmpty()){
            repository.clearQuotes()
            repository.insertQuotes(quotes.map { it.toDatabase() })
            quotes
        } else repository.getAllQuotesFromDatabase()
    }
}