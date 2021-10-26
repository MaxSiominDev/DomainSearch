package dev.maxsiomin.domainsearch.util

import android.text.Editable
import android.util.Patterns

const val PASSWORD_MIN_LENGTH = 8

data class Email(
    val value: String,
) {
    constructor(editableValue: Editable?) : this(editableValue?.toString() ?: "")

    val isCorrect = value.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(value).matches()
}

data class Password(
    val value: String,
) {
    constructor(editableValue: Editable?) : this(editableValue?.toString() ?: "")

    val isEmpty = value.isEmpty()
    val isStrong = value.length >= 8
}
