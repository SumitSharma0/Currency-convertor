package com.example.currencyconvertor.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.currencyconvertor.data.local.dao.CurrencyDao
import com.example.currencyconvertor.data.local.database.AppDatabase
import com.example.currencyconvertor.data.remote.api.CurrencyApiService
import com.example.currencyconvertor.data.remote.mapper.CurrencyMapper
import com.example.currencyconvertor.data.repository.CurrencyRepositoryImpl
import com.example.currencyconvertor.domain.repository.CurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCurrencyApiService(retrofit: Retrofit): CurrencyApiService {
        return retrofit.create(CurrencyApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            "currency_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCurrencyDao(appDatabase: AppDatabase): CurrencyDao {
        return appDatabase.currencyDao()
    }

    @Provides
    @Singleton
    fun provideCurrencyRepository(
        apiService: CurrencyApiService,
        currencyDao: CurrencyDao,
        currencyMapper: CurrencyMapper
    ): CurrencyRepository {
        return CurrencyRepositoryImpl(apiService, currencyDao, currencyMapper)
    }

}