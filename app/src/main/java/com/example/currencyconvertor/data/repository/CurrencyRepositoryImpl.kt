package com.example.currencyconvertor.data.repository

import com.example.currencyconvertor.data.local.dao.CurrencyDao
import com.example.currencyconvertor.data.local.entity.CurrencyMetadata
import com.example.currencyconvertor.data.remote.api.CurrencyApiService
import com.example.currencyconvertor.data.remote.mapper.CurrencyMapper
import com.example.currencyconvertor.domain.model.Currency
import com.example.currencyconvertor.domain.repository.CurrencyRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Implementation of the CurrencyRepository interface.
 */
class CurrencyRepositoryImpl @Inject constructor(
    private val currencyApiService: CurrencyApiService,
    private val currencyDao: CurrencyDao,
    private val currencyMapper: CurrencyMapper
) : CurrencyRepository {

    override suspend fun fetchCurrencyRates(): List<Currency> {
        val currentTime = System.currentTimeMillis()
        val lastUpdateTime = currencyDao.getLastUpdateTimestamp() ?: 0L

        return if (currentTime - lastUpdateTime >= TimeUnit.MINUTES.toMillis(30)) {
            // Fetch new data from the API if 30 minutes have passed since the last update
            val currencyResponse = currencyApiService.getLatestRates()
            val currencies = currencyResponse.rates.map { (code, rate) ->
                currencyMapper.mapToDomainModel(code, rate)
            }
            saveCurrencies(currencies) // Save the fetched data
            currencyDao.updateLastUpdatedTimestamp(CurrencyMetadata(lastUpdated = currentTime))
            currencies
        } else {
            // Return cached data
            getSavedCurrencies()
        }
    }

    override suspend fun getSavedCurrencies(): List<Currency> {
        // Retrieves saved currencies from the local database
        val res =  currencyDao.getAllCurrencies().map {
            currencyMapper.mapFromEntity(it)
        }
        return res
    }

    override suspend fun saveCurrencies(currencies: List<Currency>) {
        // Saves currency data to the local database
        val currencyEntities = currencies.map {
            currencyMapper.mapToEntity(it)
        }
        currencyDao.insertCurrencies(currencyEntities)
    }
}