package com.example.currencyconvertor.data.repository

import com.example.currencyconvertor.data.local.dao.CurrencyDao
import com.example.currencyconvertor.data.local.entity.CurrencyEntity
import com.example.currencyconvertor.data.remote.api.CurrencyApiService
import com.example.currencyconvertor.data.remote.dto.CurrencyResponseDto
import com.example.currencyconvertor.data.remote.mapper.CurrencyMapper
import com.example.currencyconvertor.domain.model.Currency
import com.example.currencyconvertor.domain.repository.CurrencyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
class CurrencyRepositoryImplTest {

    private lateinit var currencyRepository: CurrencyRepository
    private val currencyApiService: CurrencyApiService = mock()
    private val currencyDao: CurrencyDao = mock()
    private val currencyMapper: CurrencyMapper = spy()

    @Before
    fun setup() {
        currencyRepository = CurrencyRepositoryImpl(
            currencyApiService,
            currencyDao,
            currencyMapper
        )
    }

    @Test
    fun `fetchCurrencyRates should return API data when cache is outdated`() = runTest {
        whenever(currencyDao.getLastUpdateTimestamp()).thenReturn(0L)

        val apiResponse = CurrencyResponseDto("USD", mapOf("USD" to 1.0, "EUR" to 0.85, "INR" to 0.05)) // Create CurrencyResponseDto
        whenever(currencyApiService.getLatestRates()).thenReturn(apiResponse)

        val domainCurrencies = listOf(
            Currency("USD", "USA",1.0),
            Currency("EUR", "Euro",0.85),
            Currency("INR", "Indian Rupee",0.05)
        )
        whenever(currencyMapper.mapToDomainModel("USD", 1.0)).thenReturn(domainCurrencies[0])
        whenever(currencyMapper.mapToDomainModel("EUR", 0.85)).thenReturn(domainCurrencies[1])
        whenever(currencyMapper.mapToDomainModel("INR", 0.05)).thenReturn(domainCurrencies[2])

        val result = currencyRepository.fetchCurrencyRates()

        assertEquals(domainCurrencies, result)
    }

    @Test
    fun `fetchCurrencyRates should return cached data when cache is not outdated`() = runTest {
        val currentTime = System.currentTimeMillis()
        whenever(currencyDao.getLastUpdateTimestamp()).thenReturn(currentTime)

        val cachedCurrencies = listOf(
            Currency("USD", "USA", 1.0)
        )
        val currencyEntity = CurrencyEntity("USD", "USA", 1.0)
        whenever(currencyDao.getAllCurrencies()).thenReturn(listOf(currencyEntity))

        val result = currencyRepository.fetchCurrencyRates()

        assertEquals(cachedCurrencies, result)
    }

    @Test
    fun `getSavedCurrencies should return data from DAO`() = runTest {
        val cachedCurrencies = listOf(
            Currency("USD", "USA", 1.0)
        )
        val currencyEntity = CurrencyEntity("USD", "USA", 1.0)
        whenever(currencyDao.getAllCurrencies()).thenReturn(listOf(currencyEntity))

        val result = currencyRepository.getSavedCurrencies()

        assertEquals(cachedCurrencies, result)
    }

    @Test
    fun `saveCurrencies should save data to DAO`() = runTest {
        val currencies = listOf(
            Currency("USD", "USA", 1.0)
        )
        currencyRepository.saveCurrencies(currencies)
        // Add verification for interactions with currencyDao here
    }
}