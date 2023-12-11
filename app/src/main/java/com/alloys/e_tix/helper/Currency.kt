package com.alloys.e_tix.helper

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object Currency {
    fun formatToIDRCurrency(value: Int): String {
        val format = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance("IDR")

        return format.format(value)
    }

    fun formatNumberWithCommas(number: Int): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
        return numberFormat.format(number)
    }
}