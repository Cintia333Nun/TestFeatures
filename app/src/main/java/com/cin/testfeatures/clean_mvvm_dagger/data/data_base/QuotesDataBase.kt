package com.cin.testfeatures.clean_mvvm_dagger.data.data_base

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cin.testfeatures.clean_mvvm_dagger.data.data_base.dao.QuoteDao
import com.cin.testfeatures.clean_mvvm_dagger.data.data_base.entities.QuoteEntity

@Database(entities = [QuoteEntity::class], version = 1)
abstract class QuoteDatabase: RoomDatabase() {
    abstract fun getQuoteDao(): QuoteDao
}