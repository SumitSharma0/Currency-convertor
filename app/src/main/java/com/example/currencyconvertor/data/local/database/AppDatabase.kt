package com.example.currencyconvertor.data.local.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.currencyconvertor.data.local.dao.CurrencyDao
import com.example.currencyconvertor.data.local.entity.CurrencyEntity
import com.example.currencyconvertor.data.local.entity.CurrencyMetadata

@Database(entities = [CurrencyEntity::class, CurrencyMetadata::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}