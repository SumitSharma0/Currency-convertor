package com.example.currencyconvertor.data.remote.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrencyResponseDto(
    val base: String,  // The base currency, e.g., "USD"
    val rates: Map<String, Double>  // A map of currency codes to their exchange rates
): Parcelable