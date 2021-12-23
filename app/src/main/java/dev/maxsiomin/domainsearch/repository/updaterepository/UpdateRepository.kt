package dev.maxsiomin.domainsearch.repository.updaterepository

import dev.maxsiomin.domainsearch.base.BaseRepository
import dev.maxsiomin.domainsearch.extensions.notNull
import dev.maxsiomin.domainsearch.network.UpdateApi
import dev.maxsiomin.domainsearch.util.UiActions
import dev.maxsiomin.domainsearch.util.addOnCompleteListener
import timber.log.Timber

class UpdateRepository(uiActions: UiActions, private val callback: (LastVersionSearchResult) -> Unit) : BaseRepository(uiActions) {

    /**
     * Searches for last version of app on my server
     */
    fun getLastVersion() {
        UpdateApi.retrofitService.getLastVersion().addOnCompleteListener { result ->

            if (result.isSuccessful) {
                callback(Success(result.response!!.body()!!.toString()))
            } else {
                Timber.e(result.t)

                val errorMessage = result.t?.message?.notNull()
                callback(Failure(isConnectionError(errorMessage), errorMessage))
            }
        }
    }
}
