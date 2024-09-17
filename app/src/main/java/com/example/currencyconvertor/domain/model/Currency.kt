package com.example.currencyconvertor.domain.model

data class Currency(
    val code: String,      // Currency code, e.g., "USD"
    val name: String,      // Currency name, e.g., "United States Dollar"
    val rate: Double       // Exchange rate relative to a base currency

)
