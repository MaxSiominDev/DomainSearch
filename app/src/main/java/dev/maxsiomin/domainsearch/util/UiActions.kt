package dev.maxsiomin.domainsearch.util

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import dev.maxsiomin.domainsearch.annotations.ToastDuration
import dev.maxsiomin.domainsearch.database.SearchDao
import dev.maxsiomin.domainsearch.database.SearchDatabase

typealias SharedPrefs = android.content.SharedPreferences

/**
 * Use this class in order to get access to actions of user interface
 */
interface UiActions {

    val context: Context

    val sharedPrefs: SharedPrefs

    val searchDao: SearchDao

    /** Shows string from resources as toast */
    @MainThread
    fun toast(@StringRes resId: Int, @ToastDuration length: Int)

    /** Show [message] as toast */
    @MainThread
    fun toast(message: String, @ToastDuration length: Int)

    /** Gets string from resources */
    fun getString(@StringRes resId: Int, vararg args: Any): String

    /** Hides keyboard */
    fun hideKeyboard(windowToken: IBinder)
}

class UiActionsImpl(override val context: Context) : UiActions {

    override val sharedPrefs: SharedPrefs = context.getDefaultSharedPrefs()

    override val searchDao: SearchDao = SearchDatabase.getInstance(context).searchDao()

    private val inputMethodManager
        get() = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

    override fun toast(resId: Int, length: Int) =
        toast(getString(resId), length)

    override fun toast(message: String, length: Int) =
        Toast.makeText(context, message, length).show()

    private data class ResIdWithArgs(
        @StringRes val resId: Int,
        val args: List<Any>,
    )

    /**
     * Contains strings that were already loaded from resources
     */
    private val strings = mutableMapOf<ResIdWithArgs, String>()

    /**
     * If string were already loaded returns it from [strings] else uses context to get the string from resources
     */
    override fun getString(resId: Int, vararg args: Any): String {
        val resIdWithArgs = ResIdWithArgs(resId, args.toList())

        if (strings[resIdWithArgs] == null)
            strings[resIdWithArgs] = context.getString(resId)

        return strings[resIdWithArgs]!!
    }

    override fun hideKeyboard(windowToken: IBinder) {
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}
