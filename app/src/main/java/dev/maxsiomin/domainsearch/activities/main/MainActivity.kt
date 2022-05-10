package dev.maxsiomin.domainsearch.activities.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.domainsearch.APK_LOCATION
import dev.maxsiomin.domainsearch.BuildConfig
import dev.maxsiomin.domainsearch.R
import dev.maxsiomin.domainsearch.databinding.ActivityMainBinding
import dev.maxsiomin.domainsearch.extensions.openInBrowser
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var analytics: FirebaseAnalytics

    private val mViewModel by viewModels<MainViewModel>()

    /*override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate called")
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel.checkForUpdates { latestVersionName ->
            suggestUpdating(latestVersionName)
        }
    }*/

    /*private fun suggestUpdating(latestVersionName: String) {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.update_app, BuildConfig.VERSION_NAME, latestVersionName))
            .setNegativeButton(R.string.no_thanks) { _, _ -> }
            .setPositiveButton(R.string.update) { _, _ ->
                update()
            }
            .create()
    }*/

    /**
     * Opens direct uri to .apk in browser; .apk file should be automatically downloaded
     */
    /*private fun update() {
        openInBrowser(APK_LOCATION)
    }*/
}
