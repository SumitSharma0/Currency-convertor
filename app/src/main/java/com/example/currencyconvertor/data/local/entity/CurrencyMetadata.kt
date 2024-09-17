package com.example.currencyconvertor.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_metadata")
data class CurrencyMetadata(
    @PrimaryKey val id: Int = 1,
    val lastUpdated: Long
)
