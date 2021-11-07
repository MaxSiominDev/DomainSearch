package dev.maxsiomin.domainsearch.base

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.domainsearch.R
import dev.maxsiomin.domainsearch.activities.login.LoginActivity
import dev.maxsiomin.domainsearch.util.SHARED_DATA
import dev.maxsiomin.domainsearch.util.SharedData
import dev.maxsiomin.domainsearch.util.SharedDataImpl
import dev.maxsiomin.domainsearch.util.SharedPrefs
import timber.log.Timber
import javax.inject.Inject

typealias DialogBuilder = AlertDialog.Builder

const val APK_LOCATION = "https://maxsiomin.dev/apps/domain_search/domain_search.apk"

/**
 * All activities in project except [LoginActivity] must extend this class
 */
@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity(), OnSharedPreferenceChangeListener {

    lateinit var sharedData: SharedData

    private lateinit var analytics: FirebaseAnalytics

    protected open val mViewModel by viewModels<BaseViewModel>()

    protected val keyTheme: String get() = mViewModel.getString(R.string.key_theme)
    protected val themeFollowSystem = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM.toString()

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate called")
        super.onCreate(savedInstanceState)

        analytics = Firebase.analytics

        sharedData = SharedDataImpl(savedInstanceState?.getBundle(SHARED_DATA))

        mViewModel.sharedPrefs.registerOnSharedPreferenceChangeListener(this)

        setMode()

        checkLogin()
    }

    /**
     * If user isn't logged in calls [goToLoginScreen]
     */
    private fun checkLogin() {
        auth.currentUser?.reload()?.addOnFailureListener { _ ->
            goToLoginScreen()
        } ?: goToLoginScreen()
    }

    /**
     * Starts [LoginActivity] and finishes current
     */
    private fun goToLoginScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        Timber.d("onDestroy called")
        mViewModel.sharedPrefs.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        Timber.d("onSaveInstanceState called")
        outState.putBundle(SHARED_DATA, sharedData.toBundle())
        super.onSaveInstanceState(outState, outPersistentState)
    }

    /**
     * Gets mode from [SharedPrefs] and sets it via [AppCompatDelegate]
     */
    protected open fun setMode() {
        val theme: String =
            mViewModel.sharedPrefs.getString(keyTheme, themeFollowSystem) ?: themeFollowSystem
        Timber.d("Theme=$theme")
        AppCompatDelegate.setDefaultNightMode(theme.toInt())
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPrefs?, key: String?) {
        Timber.d("onSharedPreferenceChanged called")

        when (key) {
            keyTheme -> setMode()
        }
    }

    /**
     * Opens [url] via browser
     * If browser not found, toasts [R.string.smth_went_wrong]
     */
    fun openInBrowser(url: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)))
        } catch (e: ActivityNotFoundException) {
            mViewModel.toast(R.string.smth_went_wrong, Toast.LENGTH_LONG)
        }
    }

    /**
     * Logs out with [FirebaseAuth].
     * Starts [LoginActivity].
     * Finishes this activity
     */
    fun onLogout() {
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
