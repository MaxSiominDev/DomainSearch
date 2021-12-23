package dev.maxsiomin.domainsearch.extensions

import android.content.Context
import androidx.preference.PreferenceManager
import dev.maxsiomin.domainsearch.util.SharedPrefs

fun Context.getDefaultSharedPrefs(): SharedPrefs =
    PreferenceManager.getDefaultSharedPreferences(this)
