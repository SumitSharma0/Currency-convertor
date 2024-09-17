package com.example.currencyconvertor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconvertor.domain.model.Currency
import com.example.currencyconvertor.domain.usecase.FetchCurrencyRatesUseCase
import com.example.currencyconvertor.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val fetchCurrencyRatesUseCase: FetchCurrencyRatesUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount

    private val _selectedCurrency = MutableStateFlow("USD")
    val selectedCurrency: StateFlow<String> = _selectedCurrency

    private val _currencyList = MutableStateFlow<Resource<List<Currency>>>(Resource.Loading())
    val currencyList: StateFlow<Resource<List<Currency>>> = _currencyList

    private val _convertedAmounts = MutableStateFlow<List<Pair<String, Double>>>(emptyList())
    val convertedAmounts: StateFlow<List<Pair<String, Double>>> = _convertedAmounts

    // Fetch currency rates from the API and save them locally
    internal fun fetchCurrencyRates() {
        _currencyList.value = Resource.Loading()
        viewModelScope.launch(dispatcher) {
            try {
                val fetchedCurrencies = fetchCurrencyRatesUseCase.execute()
                _currencyList.value = Resource.Success(fetchedCurrencies)
            } catch (e: Exception) {
                _currencyList.value = Resource.Error("Failed to fetch currency rates: ${e.message}")
            }
        }
    }

    fun onAmountChanged(newAmount: String) {
        _amount.value = newAmount
        convertAmounts()
    }

    fun onCurrencySelected(newCurrency: String) {
        _selectedCurrency.value = newCurrency
        convertAmounts()
    }

    private fun convertAmounts() {
        val amountValue = _amount.value.toDoubleOrNull() ?: return
        val selectedRate =
            _currencyList.value.data?.find { it.code == _selectedCurrency.value }?.rate ?: return

        _convertedAmounts.value = _currencyList.value.data?.map { currency ->
            currency.code to amountValue * (currency.rate / selectedRate)
        } ?: emptyList()
    }

}


