package dev.maxsiomin.domainsearch.fragments.history

import androidx.lifecycle.MutableLiveData
import dev.maxsiomin.domainsearch.R
import dev.maxsiomin.domainsearch.database.SearchQuery
import dev.maxsiomin.domainsearch.extensions.addPrefix
import dev.maxsiomin.domainsearch.repository.domainrepository.Status
import dev.maxsiomin.domainsearch.util.UiActions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Provides search history
 */
class HistoryLoader(uiActions: UiActions) : UiActions by uiActions {

    val itemsLiveData = MutableLiveData<List<RecyclerViewItem>>()
    private val items = mutableListOf<RecyclerViewItem>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getHistory()
        }
    }

    /**
     * Loads history from Database and updates [itemsLiveData]
     */
    private suspend fun getHistory() {
        val history: List<SearchQuery> = searchDao.loadHistory()

        for (i in history.indices.reversed()) {
            val id = history[i].searchedDomainId
            val domain = searchDao.findDomainById(id)

            val description = when (history[i].status) {
                Status.FAILURE   -> getString(R.string.error)
                Status.NOT_FOUND -> getString(R.string.not_found)
                // Status.FOUND
                else -> searchDao.getDomainDescription(domain!!)!!
            }

            items.add(createPlaceholderItem(i + 1, domain!!.addPrefix("."), description))
        }

        itemsLiveData.postValue(items)
    }

    /**
     * Returns new [RecyclerViewItem]
     */
    private fun createPlaceholderItem(position: Int, domain: String, description: String) =
        RecyclerViewItem(position.toString(), domain, description)

    /**
     * A placeholder item representing a piece of content
     */
    data class RecyclerViewItem(val id: String, val domain: String, val description: String)
}
