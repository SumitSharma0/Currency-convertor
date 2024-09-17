package com.example.currencyconvertor.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.currencyconvertor.presentation.viewmodel.CurrencyViewModel
import com.example.currencyconvertor.presentation.screens.CurrencyConverterScreen
import com.example.currencyconvertor.presentation.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel = hiltViewModel<CurrencyViewModel>()
                    viewModel.fetchCurrencyRates()
                    CurrencyConverterScreen(viewModel = viewModel)
                    NavHost(navController = navController, startDestination = "converter") {
                        composable("converter") {

                        }
                    }
                }
            }
        }
    }
}