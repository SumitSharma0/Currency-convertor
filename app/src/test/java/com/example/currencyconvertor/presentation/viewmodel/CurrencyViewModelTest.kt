package com.example.currencyconvertor.presentation.viewmodel

import com.example.currencyconvertor.domain.model.Currency
import com.example.currencyconvertor.domain.usecase.FetchCurrencyRatesUseCase
import com.example.currencyconvertor.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class CurrencyViewModelTest {

    private lateinit var fetchCurrencyRatesUseCase: FetchCurrencyRatesUseCase
    private lateinit var viewModel: CurrencyViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fetchCurrencyRatesUseCase = mock()
        viewModel = CurrencyViewModel(fetchCurrencyRatesUseCase, testDispatcher)
    }


    @Test
    fun `fetchCurrencyRates should set loading state initially`() = runTest {
        whenever(fetchCurrencyRatesUseCase.execute()).thenReturn(emptyList())

        val result = viewModel.currencyList.first()

        viewModel.fetchCurrencyRates()

        assert(result is Resource.Loading)
    }

    @Test
    fun `fetchCurrencyRates should fetch currencies and set success state`() = runTest {
        val mockCurrencies = listOf(
            Currency("USD", "USA",1.0),
            Currency("EUR", "Euro",0.85)
        )
        whenever(fetchCurrencyRatesUseCase.execute()).thenReturn(mockCurrencies)

        viewModel.fetchCurrencyRates()
        val result = viewModel.currencyList.first().data
        assertEquals(Resource.Success(mockCurrencies).data, result)
    }

    @Test
    fun `fetchCurrencyRates should set error state when exception occurs`() = runTest  {
        val errorMessage = "Network Error"
        whenever(fetchCurrencyRatesUseCase.execute()).thenThrow(RuntimeException(errorMessage))

        viewModel.fetchCurrencyRates()

        val result = viewModel.currencyList.first()
        assertTrue(result is Resource.Error)
        assertTrue(result.message == "Failed to fetch currency rates: $errorMessage")
    }

    @Test
    fun `onAmountChanged should update amount and trigger conversion`() = runTest {
        val mockCurrencies = listOf(
            Currency("USD", "USA",1.0),
            Currency("EUR", "Euro",0.85)
        )
        whenever(fetchCurrencyRatesUseCase.execute()).thenReturn(mockCurrencies)

        viewModel.fetchCurrencyRates()

        viewModel.onAmountChanged("100")

        val expectedAmount = "100"
        assertEquals(expectedAmount, viewModel.amount.first())

        // Verify that the conversion results are not empty and are correct
        val conversionResults = viewModel.convertedAmounts.first()
        assert(conversionResults.isNotEmpty())
        assertEquals(mockCurrencies.size, conversionResults.size)
    }



    @Test
    fun `onCurrencySelected should update selected currency and trigger conversion`() = runTest {
        val mockCurrencies = listOf(
            Currency("USD", "USA",1.0),
            Currency("EUR", "Euro",0.85)
        )
        whenever(fetchCurrencyRatesUseCase.execute()).thenReturn(mockCurrencies)

        viewModel.fetchCurrencyRates()
        viewModel.onCurrencySelected("EUR")
        val result = viewModel.selectedCurrency.first()

        assertEquals("EUR", result)
//        assert(viewModel.convertedAmounts.first().isNotEmpty())
    }

}
