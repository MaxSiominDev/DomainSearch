package dev.maxsiomin.domainsearch.repository.updaterepository

/*class UpdateRepository(uiActions: UiActions, private val callback: (LastVersionSearchResult) -> Unit) : BaseRepository(uiActions) {

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
}*/
