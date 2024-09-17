package com.example.currencyconvertor.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = false)
    val code: String,
    val name: String,
    val rate: Double
)