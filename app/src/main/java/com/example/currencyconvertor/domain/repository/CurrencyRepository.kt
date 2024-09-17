package com.example.currencyconvertor.domain.repository

import com.example.currencyconvertor.domain.model.Currency

/**
 *
 * An interface that provides methods for accessing currency data.
 *
 */
interface CurrencyRepository {
    suspend fun fetchCurrencyRates(): List<Currency>
    suspend fun getSavedCurrencies(): List<Currency>
    suspend fun saveCurrencies(currencies: List<Currency>)
}