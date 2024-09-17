package com.example.currencyconvertor.domain.usecase

import com.example.currencyconvertor.domain.model.Currency
import com.example.currencyconvertor.domain.repository.CurrencyRepository
import javax.inject.Inject

class FetchCurrencyRatesUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend fun execute(): List<Currency> {
        // Fetches the currency rates from the API and save them locally
        val currencies = currencyRepository.fetchCurrencyRates()
        currencyRepository.saveCurrencies(currencies)
        return currencies
    }
}