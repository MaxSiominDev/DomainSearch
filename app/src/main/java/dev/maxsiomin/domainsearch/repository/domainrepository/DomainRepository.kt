package dev.maxsiomin.domainsearch.repository.domainrepository

import dev.maxsiomin.domainsearch.base.BaseRepository
import dev.maxsiomin.domainsearch.database.Domain
import dev.maxsiomin.domainsearch.network.DomainsApi
import dev.maxsiomin.domainsearch.network.DomainsApiQueryResult
import dev.maxsiomin.domainsearch.util.UiActions
import dev.maxsiomin.domainsearch.util.addOnCompleteListener
import dev.maxsiomin.domainsearch.util.notNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

private const val EMPTY = "-"
private const val ERROR = "e"

class DomainRepository @Inject constructor(
    uiActions: UiActions,
    private val callback: (DomainSearchResult) -> Unit,
) : BaseRepository(uiActions) {

    /**
     * First tries to get description of [domain] from database.
     * If description is not in database tries to download it
     */
    suspend fun getDescription(domain: String) {
        val descriptionFromDatabase = searchDao.getDomainDescription(domain)

        if (descriptionFromDatabase !in listOf(null, ERROR)) {
            callback(
                Success(
                    domain = domain,
                    description = descriptionFromDatabase,
                    status = if (descriptionFromDatabase != EMPTY) Status.FOUND else Status.NOT_FOUND,
                )
            )
            return
        }
        download(domain)
    }

    /**
     * Gets description via retrofit
     */
    private fun download(domain: String) {
        DomainsApi.retrofitService.downloadDescription(domain).addOnCompleteListener { result ->

            if (result.isSuccessful) {
                onSuccess(domain, result.response!!)
            } else {
                onFailure(domain, result.t)
            }
        }
    }

    private fun onSuccess(domain: String, response: Response<DomainsApiQueryResult>) {
        CoroutineScope(Dispatchers.IO).launch {

            val body = response.body()!!

            val description = if (response.isSuccessful) {
                body.result ?: "fuckingHell"
            } else {
                EMPTY
            }

            with (searchDao) {
                val id = findIdByDomain(domain) ?: 0
                insertDomain(Domain(id, domain, description))
            }

            val status =
                when (description) {
                    EMPTY -> Status.NOT_FOUND
                    ERROR -> Status.FAILURE
                    else -> Status.FOUND
                }

            callback(
                if (status != Status.FAILURE)
                    Success(domain, description, status)
                else
                    Failure(domain, false, null)
            )
        }
    }

    private fun onFailure(domain: String, t: Throwable?) {
        Timber.e(t)

        CoroutineScope(Dispatchers.IO).launch {
            searchDao.insertDomain(Domain(0, domain, ERROR))

            val errorMessage = t?.message.notNull()
            callback(
                Failure(domain, isConnectionError(errorMessage), errorMessage)
            )
        }
    }
}
