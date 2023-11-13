package com.cin.testfeatures.clean_mvvm_dagger.data

import com.cin.testfeatures.clean_mvvm_dagger.data.data_base.dao.QuoteDao
import com.cin.testfeatures.clean_mvvm_dagger.data.data_base.entities.QuoteEntity
import com.cin.testfeatures.clean_mvvm_dagger.data.model.QuoteModel
import com.cin.testfeatures.clean_mvvm_dagger.data.network.QuoteService
import com.cin.testfeatures.clean_mvvm_dagger.domain.model.Quote
import com.cin.testfeatures.clean_mvvm_dagger.domain.model.toDomain
import javax.inject.Inject

class QuoteRepository @Inject constructor(
    private val api: QuoteService,
    private val quoteDao: QuoteDao
) {

    suspend fun getAllQuotesFromApi(): List<Quote> {
        val response: List<QuoteModel> = api.getQuotes()
        return response.map { it.toDomain() }
    }

    suspend fun getAllQuotesFromDatabase():List<Quote>{
        val response: List<QuoteEntity> = quoteDao.getAllQuotes()
        return response.map { it.toDomain() }
    }

    suspend fun insertQuotes(quotes:List<QuoteEntity>) = quoteDao.insertAll(quotes)

    suspend fun clearQuotes() = quoteDao.deleteAllQuotes()
}