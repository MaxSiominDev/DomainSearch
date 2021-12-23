package dev.maxsiomin.domainsearch.fragments.history

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.domainsearch.R
import dev.maxsiomin.domainsearch.base.BaseFragment
import dev.maxsiomin.domainsearch.databinding.FragmentHistoryBinding
import dev.maxsiomin.domainsearch.util.UiActions
import javax.inject.Inject

/**
 * Fragment with search history
 */
@AndroidEntryPoint
class HistoryFragment : BaseFragment(R.layout.fragment_history) {

    override var _binding: ViewBinding? = null
    private val binding get() = _binding as FragmentHistoryBinding

    @Inject
    lateinit var uiActions: UiActions

    private lateinit var historyLoader: HistoryLoader

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHistoryBinding.bind(view)

        historyLoader = HistoryLoader(uiActions)
        historyLoader.itemsLiveData.observe(viewLifecycleOwner) {
            onHistoryUpdated(it)
        }
    }

    private fun onHistoryUpdated(history: List<HistoryLoader.PlaceholderItem>?) {
        // If there is no history in database
        if (history.isNullOrEmpty()) {
            binding.historyIsEmptyTextView.visibility = View.VISIBLE
            return
        }

        // Set the adapter
        binding.historyRecyclerView.adapter = HistoryItemRecyclerViewAdapter(uiActions, history)
    }
}
