package dev.maxsiomin.domainsearch.activities.main

import android.app.Dialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.domainsearch.APK_LOCATION
import dev.maxsiomin.domainsearch.BuildConfig
import dev.maxsiomin.domainsearch.R
import dev.maxsiomin.domainsearch.activities.Updater
import dev.maxsiomin.domainsearch.databinding.ActivityMainBinding
import dev.maxsiomin.domainsearch.extensions.openInBrowser
import dev.maxsiomin.domainsearch.util.SharedPrefsConfig.DATE_UPDATE_SUGGESTED
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

typealias DialogBuilder = AlertDialog.Builder

/**
 * Main activity of application.
 * Implements [Updater] interface in order to check updates every app launch.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Updater {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var analytics: FirebaseAnalytics

    @Inject
    lateinit var auth: FirebaseAuth

    private val mViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate called")
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel.checkForUpdates { latestVersionName ->
            suggestUpdating(latestVersionName)
        }
    }

    /**
     * Suggests user to update app via [UpdateDialog]
     */
    private fun suggestUpdating(latestVersionName: String) {
        // Save when update was suggested last time
        mViewModel.sharedPrefs.edit().apply {
            putString(DATE_UPDATE_SUGGESTED, LocalDate.now().toString())
        }.apply()

        UpdateDialog.newInstance(latestVersionName).show(supportFragmentManager)
    }

    /**
     * Opens direct uri to .apk in browser. .apk should be automatically downloaded
     */
    override fun update() {
        openInBrowser(APK_LOCATION)
    }

    class UpdateDialog : DialogFragment() {

        private val updater get() = activity as Updater

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
