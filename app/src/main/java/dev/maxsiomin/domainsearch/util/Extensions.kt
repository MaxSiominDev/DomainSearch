package dev.maxsiomin.domainsearch.util

import android.content.Context
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.textfield.TextInputLayout
import java.lang.StringBuilder

/**
 * Adds [surroundWith] to the start and to the end of string
 */
fun String.surroundWith(surroundWith: Char): String =
    surroundWith + this + surroundWith

/**
 * Returns true if string only contains letters of latin ABC and its length is more than 2
 */
fun String.isCorrectAsDomain(): Boolean {
    val domain = if (this.startsWith(".")) this.slice(1 until length) else this

    if (domain.length >= 2)
        return !domain.containsNot('a'..'z')

    return false
}

/**
 * If string contains symbol that is not in [charRange] returns true
 */
fun String.containsNot(charRange: CharRange): Boolean {
    forEach {
        if (it !in charRange) return true
    }
    return false
}

/**
 * Returns [prefix] + string
 */
fun String.addPrefix(prefix: String): String = prefix + this

/**
 * If string == null, returns ""
 */
fun String?.notNull(): String = this ?: ""

/**
 * Removes [char] from string even if the string contains this char several times
 */
fun String.removeAll(char: Char): String {
    var result: String

    do {
        result = this.replace(char.toString(), "")
    } while (char in result)

    return result
}

fun String.toEditable() = SpannableStringBuilder(this)

/**
 * error = null
 */
fun TextInputLayout.clearError() {
    error = null
}

fun Context.getDefaultSharedPrefs(): SharedPrefs =
    PreferenceManager.getDefaultSharedPreferences(this)

/**
 * Returns true if string contains at least 1 symbol from charRange
 */
operator fun String.contains(other: CharRange): Boolean {
    val ss = StringBuilder(this)
    ss.forEach {
        if (it in other)
            return true
    }
    return false
}
