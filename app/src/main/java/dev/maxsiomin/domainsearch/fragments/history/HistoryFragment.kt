package dev.maxsiomin.domainsearch.fragments.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
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
class HistoryFragment : BaseFragment(R.layout.fragment_history, true) {

    private var columnCount = 1

    override var _binding: ViewDataBinding? = null
    private val binding get() = _binding!! as FragmentHistoryBinding

    @Inject
    lateinit var uiActions: UiActions

    private lateinit var historyLoader: HistoryLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        historyLoader = HistoryLoader(uiActions)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        super.onCreateView(inflater, container, savedInstanceState)

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        historyLoader.itemsLiveData.observe(viewLifecycleOwner) { onHistoryUpdated(it) }

        return binding.root
    }

    private fun onHistoryUpdated(history: List<HistoryLoader.PlaceholderItem>?) {
        with (binding) {
            // If there is no history in database
            if (history.isNullOrEmpty()) {
                historyIsEmptyTextView.visibility = View.VISIBLE
                return
            }

            // Set the adapter
            historyRecyclerView.layoutManager = GridLayoutManager(context, columnCount)
            historyRecyclerView.adapter = HistoryItemRecyclerViewAdapter(uiActions, history)
        }
    }

    companion object {
        private const val ARG_COLUMN_COUNT = "columnCount"
    }
}
