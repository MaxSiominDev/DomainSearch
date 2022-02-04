package dev.maxsiomin.domainsearch.activities.main

import android.widget.Toast
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.domainsearch.BuildConfig
import dev.maxsiomin.domainsearch.R
import dev.maxsiomin.domainsearch.base.BaseViewModel
import dev.maxsiomin.domainsearch.repository.updaterepository.Failure
import dev.maxsiomin.domainsearch.repository.updaterepository.Success
import dev.maxsiomin.domainsearch.repository.updaterepository.UpdateRepository
import dev.maxsiomin.domainsearch.util.SharedPrefsConfig.DATE_UPDATE_SUGGESTED
import dev.maxsiomin.domainsearch.util.UiActions
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(uiActions: UiActions) : BaseViewModel(uiActions) {

    /**
     * Checks for updates. If updates found calls [onUpdateFound]
     */
    fun checkForUpdates(onUpdateFound: (String) -> Unit) {

        if (LocalDate.now().toString() == sharedPrefs.getString(DATE_UPDATE_SUGGESTED, null))
            return

        val repository = UpdateRepository(this as UiActions) { result ->
            if (result is Success) {
                val currentVersionName = BuildConfig.VERSION_NAME
                if (currentVersionName != result.latestVersionName) {
                    onUpdateFound(result.latestVersionName)
                }
            } else {
                toast(R.string.last_version_checking_failed, Toast.LENGTH_LONG)
                Timber.e((result as Failure).errorMessage)
            }
        }

        repository.getLastVersion()
    }
}
