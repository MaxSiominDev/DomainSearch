package dev.maxsiomin.domainsearch.activities.main

import android.app.Activity
import android.app.Dialog
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
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.domainsearch.BuildConfig
import dev.maxsiomin.domainsearch.R
import dev.maxsiomin.domainsearch.activities.Updater
import dev.maxsiomin.domainsearch.activities.login.LoginActivity
import dev.maxsiomin.domainsearch.databinding.ActivityMainBinding
import dev.maxsiomin.domainsearch.util.SHARED_DATA
import dev.maxsiomin.domainsearch.util.SharedData
import dev.maxsiomin.domainsearch.util.SharedDataImpl
import dev.maxsiomin.domainsearch.util.SharedPrefs
import timber.log.Timber
import javax.inject.Inject

const val APK_LOCATION = "https://maxsiomin.dev/apps/domain_search/domain_search.apk"

typealias DialogBuilder = AlertDialog.Builder

/**
 * Main activity of application. Contains 3 fragments: SearchFragment, HistoryFragment, SettingsFragment.
 * Implements [Updater] interface in order to check updates every app launch.
 * Implements [OnSharedPreferenceChangeListener] in order to listen settings changes
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Updater, OnSharedPreferenceChangeListener {

    private lateinit var binding: ActivityMainBinding

    lateinit var sharedData: SharedData

    private lateinit var analytics: FirebaseAnalytics

    @Inject
    lateinit var auth: FirebaseAuth

    val mViewModel by viewModels<MainViewModel>()

    private val keyTheme: String get() = mViewModel.getString(R.string.key_theme)
    private val themeFollowSystem = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate called")
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        analytics = Firebase.analytics
        sharedData = SharedDataImpl(savedInstanceState?.getBundle(SHARED_DATA))


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_activity_fragment_container) as NavHostFragment

        binding.navView.setupWithNavController(navHostFragment.navController)

        mViewModel.checkForUpdates { latestVersionName -> suggestUpdating(latestVersionName) }
        mViewModel.sharedPrefs.registerOnSharedPreferenceChangeListener(this)

        setupMode()

        checkLogin()
    }

    override fun onDestroy() {
        Timber.d("onDestroy called")
        mViewModel.sharedPrefs.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }

    /**
    * Gets mode from [SharedPrefs] and sets it via [AppCompatDelegate]
    */
    private fun setupMode() {
        val theme: String =
            mViewModel.sharedPrefs.getString(keyTheme, themeFollowSystem) ?: themeFollowSystem
        Timber.d("Theme=$theme")
        AppCompatDelegate.setDefaultNightMode(theme.toInt())
    }


    /**
     * Suggests user to update app via [UpdateDialog]
     */
    private fun suggestUpdating(latestVersionName: String) {
        UpdateDialog.newInstance(latestVersionName).show(supportFragmentManager)
    }

    /**
     * Opens direct uri to .apk in browser. .apk should be automatically downloaded
     */
    override fun update() {
        openInBrowser(APK_LOCATION)
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
     * Starts [LoginActivity] and finishes this
     */
    private fun goToLoginScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
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

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPrefs?, key: String?) {
        Timber.d("onSharedPreferenceChanged called")

        when (key) {
            keyTheme -> setupMode()
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        Timber.d("onSaveInstanceState called")
        outState.putBundle(SHARED_DATA, sharedData.toBundle())
        super.onSaveInstanceState(outState, outPersistentState)
    }

    class UpdateDialog : DialogFragment() {

        private val updater get() = requireActivity() as Updater

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            val currentVersionName = BuildConfig.VERSION_NAME
            val latestVersionName = requireArguments().getString(LATEST_VERSION_NAME)

            val dialog = DialogBuilder(requireContext())
                .setMessage(getString(R.string.update_app, currentVersionName, latestVersionName))
                .setNegativeButton(R.string.no_thanks) { _, _ -> }
                .setPositiveButton(R.string.update) { _, _ ->
                    updater.update()
                }
                .create()

            dialog.setCanceledOnTouchOutside(false)

            return dialog
        }

        fun show(manager: FragmentManager) {
            show(manager, TAG)
        }

        companion object {

            const val TAG = "UpdateDialog"

            /** Key for args */
            private const val LATEST_VERSION_NAME = "latestVersion"

            /**
             * Puts [latestVersionName] to args
             */
            @JvmStatic
            fun newInstance(latestVersionName: String): UpdateDialog {
                val dialogFragment = UpdateDialog()

                val args = Bundle()
                args.putString(LATEST_VERSION_NAME, latestVersionName)
                dialogFragment.arguments = args

                return dialogFragment
            }
        }
    }
}

/**
 * Opens [url] via browser
 * If browser not found, toasts [R.string.smth_went_wrong]
 */
fun Activity.openInBrowser(url: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)))
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, R.string.smth_went_wrong, Toast.LENGTH_LONG).show()
    }
}
