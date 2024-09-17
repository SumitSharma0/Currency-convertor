package com.example.currencyconvertor.data.remote.mapper

import com.example.currencyconvertor.data.local.entity.CurrencyEntity
import com.example.currencyconvertor.domain.model.Currency
import javax.inject.Inject

class CurrencyMapper @Inject constructor() {

    fun mapToDomainModel(code: String, rate: Double): Currency {
        return Currency(
            code = code,
            name = getCurrencyName(code),  // Assuming you have a method to get the full name from the code
            rate = rate
        )
    }

    fun mapFromEntity(entity: CurrencyEntity): Currency {
        return Currency(
            code = entity.code,
            name = entity.name,
            rate = entity.rate
        )
    }

    fun mapToEntity(domainModel: Currency): CurrencyEntity {
        return CurrencyEntity(
            code = domainModel.code,
            name = domainModel.name,
            rate = domainModel.rate
        )
    }

    private fun getCurrencyName(code: String): String {
        // Logic to get the full name of the currency from the code, if needed
        return when (code) {
            "USD" -> "United States Dollar"
            "EUR" -> "Euro"
            // Add other currency codes and names as needed
            else -> "Unknown Currency"
        }
    }
}