package com.example.currencyconvertor.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.currencyconvertor.domain.model.Currency

@Composable
fun CurrencyDropdownMenu(
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit,
    currencyList: List<Currency>
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)

    )
    {
        OutlinedButton(
            border = BorderStroke(1.dp, Color.Gray),
            shape = RoundedCornerShape(10), // = 50% percent
            modifier = Modifier.fillMaxWidth(),

            onClick = { expanded = !expanded },
        ) {
            Text(text = selectedCurrency, modifier = Modifier)
            Icon(
                Icons.Filled.ArrowDropDown,
                contentDescription = null,
                Modifier.rotate(if (expanded) 180f else 0f)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencyList.forEach { currency ->
                DropdownMenuItem(
                    text = { Text(text = currency.code) },
                    onClick = {
                        onCurrencySelected(currency.code)
                        expanded = false
                    },
                    modifier = Modifier.width(100.dp)
                )
            }
        }
    }
}