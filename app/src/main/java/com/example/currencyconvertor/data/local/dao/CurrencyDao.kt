package com.example.currencyconvertor.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyconvertor.data.local.entity.CurrencyEntity
import com.example.currencyconvertor.data.local.entity.CurrencyMetadata

/**
 * Data Access Object for interacting with the currency database.
 */
@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: List<CurrencyEntity>)

    @Query("SELECT * FROM currencies")
    suspend fun getAllCurrencies(): List<CurrencyEntity>

    @Query("SELECT * FROM currencies WHERE code = :code")
    suspend fun getCurrencyByCode(code: String): CurrencyEntity?

    @Query("SELECT lastUpdated FROM currency_metadata LIMIT 1")
    suspend fun getLastUpdateTimestamp(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLastUpdatedTimestamp(metadata: CurrencyMetadata)
}





