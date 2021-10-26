package dev.maxsiomin.domainsearch.fragments.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.domainsearch.BuildConfig
import dev.maxsiomin.domainsearch.R
import dev.maxsiomin.domainsearch.base.APK_LOCATION
import dev.maxsiomin.domainsearch.base.BaseActivity
import dev.maxsiomin.domainsearch.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val DEVELOPER_EMAIL = "max@maxsiomin.dev"
private const val DEVELOPER_WEBSITE = "https://maxsiomin.dev/"

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val mViewModel by viewModels<BaseViewModel>()
    private val baseActivity get() = requireActivity() as BaseActivity

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        // Set onClickListeners for all buttons
        findPreference(R.string.key_clear_history).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val historyExists = mViewModel.searchDao.getHistoryLength() > 0

                if (historyExists)
                    mViewModel.searchDao.clearHistory()

                val toastString =
                    if (historyExists)
                        R.string.history_cleared
                    else
                        R.string.empty_history

                requireActivity().runOnUiThread {
                    mViewModel.toast(toastString, Toast.LENGTH_SHORT)
                }
            }
        }

        val verifyEmailPreference = findPreference(R.string.key_verify_email)
        verifyEmailPreference.setOnClickListener {
            auth.currentUser!!.sendEmailVerification().addOnCompleteListener { task ->
                mViewModel.toast(
                    if (task.isSuccessful) R.string.check_email else R.string.unable_to_send_email_verification,
                    Toast.LENGTH_LONG
                )
            }
        }
        // If email already verified email verification shouldn't be sent
        verifyEmailPreference.isEnabled = !auth.currentUser!!.isEmailVerified

        findPreference(R.string.key_log_out).setOnClickListener {
            auth.signOut()
            baseActivity.onLogout()
        }

        findPreference(R.string.key_help_and_feedback).setOnClickListener {
            sendEmail()
        }

        findPreference(R.string.key_share_this_app).setOnClickListener {
            shareThisApp()
        }

        findPreference(R.string.key_more_apps).setOnClickListener {
            moreApps()
        }

        findPreference(R.string.key_app_version).summary = BuildConfig.VERSION_NAME
    }

    private fun findPreference(key: Int): Preference =
        super.findPreference(mViewModel.getString(key))!!

    /**
     * Open mail client and write letter to me
     */
    private fun sendEmail() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("mailto:$DEVELOPER_EMAIL")))
        } catch (e: ActivityNotFoundException) {
            mViewModel.toast(R.string.mail_client_not_found, Toast.LENGTH_SHORT)
        }
    }

    /**
     * Share link to .apk
     */
    private fun shareThisApp() {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, APK_LOCATION)
            type = "text/plain"
        }

        try {
            startActivity(Intent.createChooser(intent, null))
        } catch (e: ActivityNotFoundException) {
            mViewModel.toast(R.string.smth_went_wrong, Toast.LENGTH_LONG)
        }
    }

    private fun moreApps() {
        baseActivity.openInBrowser(DEVELOPER_WEBSITE)
    }

    private fun Preference.setOnClickListener(onClick: () -> Unit) {
        this.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            onClick()
            true
        }
    }
}
