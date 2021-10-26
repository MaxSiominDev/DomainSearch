package dev.maxsiomin.domainsearch.activities.main

import android.app.Dialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.domainsearch.BuildConfig
import dev.maxsiomin.domainsearch.R
import dev.maxsiomin.domainsearch.activities.Updater
import dev.maxsiomin.domainsearch.base.APK_LOCATION
import dev.maxsiomin.domainsearch.base.BaseActivity
import dev.maxsiomin.domainsearch.base.DialogBuilder
import dev.maxsiomin.domainsearch.databinding.ActivityMainBinding

/**
 * Main activity of application. Contains 3 fragments: SearchFragment, HistoryFragment, SettingsFragment
 * Implements [Updater] interface in order to check updates every app launch
 */
@AndroidEntryPoint
class MainActivity : BaseActivity(), Updater {

    private lateinit var binding: ActivityMainBinding

    override val mViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment =
            fragmentManager.findFragmentById(R.id.main_activity_fragment_container) as NavHostFragment

        navView.setupWithNavController(navHostFragment.navController)

        mViewModel.checkForUpdates { latestVersionName ->
            suggestUpdating(latestVersionName)
        }
    }

    /**
     * Suggests user to update app via [UpdateDialog]
     */
    private fun suggestUpdating(latestVersionName: String) {
        val dialog = UpdateDialog.newInstance(latestVersionName)
        dialog.show(supportFragmentManager)
    }

    /**
     * Opens direct uri to .apk in browser. .apk should be automatically downloaded
     */
    override fun update() {
        openInBrowser(APK_LOCATION)
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
