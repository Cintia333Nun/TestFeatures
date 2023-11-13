package com.cin.testfeatures.clean_mvvm_dagger.domain

import com.cin.testfeatures.clean_mvvm_dagger.data.QuoteRepository
import com.cin.testfeatures.clean_mvvm_dagger.domain.model.Quote
import javax.inject.Inject

class GetRandomQuoteUseCase @Inject constructor(private val repository: QuoteRepository) {
    suspend operator fun invoke(): Quote? {
        val quotes = repository.getAllQuotesFromDatabase()
        if (quotes.isNotEmpty()) {
            val randomNumber = (quotes.indices).random()
            return quotes[randomNumber]
        }
        return null
    }
}