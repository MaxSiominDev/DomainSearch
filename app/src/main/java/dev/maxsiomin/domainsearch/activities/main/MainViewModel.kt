package dev.maxsiomin.domainsearch.activities.main

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.domainsearch.base.BaseViewModel
import dev.maxsiomin.domainsearch.util.UiActions
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(uiActions: UiActions) : BaseViewModel(uiActions) {

    /**
     * Checks for updates. If updates found calls [onUpdateFound]
     */
    /*fun checkForUpdates(onUpdateFound: (String) -> Unit) {

        if (LocalDate.now().toString() == sharedPrefs.getString(TIME_UPDATE_SUGGESTED, null))
            return

        val repository = UpdateRepository(this as UiActions) { result ->
            if (result is Success) {
                val currentVersionName = BuildConfig.VERSION_NAME
                if (currentVersionName != result.latestVersionName) {
                    // Save end of today
                    sharedPrefs.edit()
                        .putLong(TIME_UPDATE_SUGGESTED, LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.UTC) + ONE_DAY)
                        .apply()

                    onUpdateFound(result.latestVersionName)
                }
            } else {
                toast(R.string.last_version_checking_failed, Toast.LENGTH_LONG)
                Timber.e((result as Failure).errorMessage)
            }
        }

        repository.getLastVersion()
    }*/

    /*companion object {
        private const val ONE_DAY = 60L * 60L * 24L
    }*/
}
