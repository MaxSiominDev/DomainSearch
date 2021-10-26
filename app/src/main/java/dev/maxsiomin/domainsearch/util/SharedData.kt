package dev.maxsiomin.domainsearch.util

import android.os.Bundle
import dev.maxsiomin.domainsearch.activities.login.LoginActivity
import dev.maxsiomin.domainsearch.base.BaseActivity
import dev.maxsiomin.domainsearch.base.BaseFragment

typealias StringSharedDataKey = SharedDataKey<String>

val BaseFragment.sharedData: SharedData
    get() {
        return if (usedByBaseActivity)
            (requireActivity() as BaseActivity).sharedData
        else
            (requireActivity() as LoginActivity).sharedData
    }

/**
 * Lets fragment save data in activity
 */
interface SharedData {

    val sharedBundle: Bundle

    fun getSharedString(key: StringSharedDataKey): String?

    fun putSharedString(key: StringSharedDataKey, value: String?)
}

class SharedDataImpl(bundle: Bundle?) : SharedData {

    override val sharedBundle: Bundle = bundle ?: Bundle()

    override fun getSharedString(key: StringSharedDataKey): String? =
        sharedBundle.getString(key.value)

    override fun putSharedString(key: StringSharedDataKey, value: String?) {
        if (value == null)
            sharedBundle.remove(key.value)
        else
            sharedBundle.putString(key.value, value)
    }
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
