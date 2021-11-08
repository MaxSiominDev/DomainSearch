package dev.maxsiomin.domainsearch.util

import android.os.Bundle
import dev.maxsiomin.domainsearch.activities.login.LoginActivity
import dev.maxsiomin.domainsearch.activities.main.MainActivity
import dev.maxsiomin.domainsearch.base.BaseFragment
import java.lang.IllegalStateException

typealias StringSharedDataKey = SharedDataKey<String>

const val SHARED_DATA = "sharedData"

val BaseFragment.sharedData: SharedData
    get() {
        return when (val activity = requireActivity()) {
            is MainActivity -> activity.sharedData
            is LoginActivity -> activity.sharedData
            else -> throw IllegalStateException()
        }
    }

/**
 * Lets fragment save data in activity
 */
interface SharedData {

    fun getSharedString(key: StringSharedDataKey): String?

    fun putSharedString(key: StringSharedDataKey, value: String?)

    fun toBundle(): Bundle
}

class SharedDataImpl(bundle: Bundle?) : SharedData {

    private val sharedBundle: Bundle = bundle ?: Bundle()

    override fun getSharedString(key: StringSharedDataKey): String? =
        sharedBundle.getString(key.value)

    override fun putSharedString(key: StringSharedDataKey, value: String?) {
        if (value == null)
            sharedBundle.remove(key.value)
        else
            sharedBundle.putString(key.value, value)
    }

    override fun toBundle(): Bundle = sharedBundle
}

@Suppress("unused")
data class SharedDataKey <T> (
    val value: String,
)

object SharedDataKeys {

    val QUERY = StringSharedDataKey("query")
    val EMAIL = StringSharedDataKey("email")
    val PASSWORD = StringSharedDataKey("password")
}
