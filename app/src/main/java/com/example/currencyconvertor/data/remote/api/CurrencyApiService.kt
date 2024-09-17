package com.example.currencyconvertor.data.remote.api

import com.example.currencyconvertor.BuildConfig
import com.example.currencyconvertor.data.remote.dto.CurrencyResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * An interface for interacting with the Open Exchange Rates API.
 */
interface CurrencyApiService {

    @GET("latest.json")
    suspend fun getLatestRates(
        @Query("app_id") appId: String = BuildConfig.API_KEY,
        @Query("base") baseCurrency: String = "USD"  // Base currency, default is USD
    ): CurrencyResponseDto

}