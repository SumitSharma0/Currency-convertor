package com.example.currencyconvertor.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconvertor.domain.model.Currency
import com.example.currencyconvertor.presentation.viewmodel.CurrencyViewModel
import com.example.currencyconvertor.utils.Resource

@Composable
fun CurrencyConverterScreen(
    viewModel: CurrencyViewModel
) {
    val amount by viewModel.amount.collectAsState()
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()
    val currencyList: Resource<List<Currency>> by viewModel.currencyList.collectAsState()
    val convertedAmounts by viewModel.convertedAmounts.collectAsState()

    when (currencyList) {
        is Resource.Error -> {
            val errorMessage = (currencyList as Resource.Error).message
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "Error: $errorMessage", color = androidx.compose.ui.graphics.Color.Red)
            }
        }

        is Resource.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(70.dp),
                )
            }

        }

        is Resource.Success -> {
            val currencies = (currencyList as Resource.Success).data
            Column(modifier = Modifier.fillMaxSize()) {
                AmountInputField(
                    amount = amount,
                    onAmountChange = { viewModel.onAmountChanged(it) }
                )
                if (currencies != null) {
                    CurrencyDropdownMenu(
                        selectedCurrency = selectedCurrency,
                        onCurrencySelected = { viewModel.onCurrencySelected(it) },
                        currencyList = currencies
                    )
                }
                CurrencyListScreen(convertedAmounts = convertedAmounts)
            }
        }
    }
}