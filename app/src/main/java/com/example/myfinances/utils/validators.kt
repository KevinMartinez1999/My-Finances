package com.example.myfinances.utils

import android.util.Patterns

fun nameValidator(text: String): Boolean {
    return ((text.length >= MIN_LENGHT_USER) || text.isEmpty())
}

fun emailValidator(text: String): Boolean {
    val pattern = Patterns.EMAIL_ADDRESS
    return (pattern.matcher(text).matches() || text.isEmpty())
}

fun passValidator(text: String): Boolean {
    return ((text.length >= MIN_LENGHT_PASS) || text.isEmpty())
}