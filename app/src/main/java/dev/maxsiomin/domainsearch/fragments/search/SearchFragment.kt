package dev.maxsiomin.domainsearch.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.domainsearch.R
import dev.maxsiomin.domainsearch.base.BaseFragment
import dev.maxsiomin.domainsearch.databinding.FragmentSearchBinding
import dev.maxsiomin.domainsearch.util.SharedDataKeys.QUERY
import dev.maxsiomin.domainsearch.util.notNull
import dev.maxsiomin.domainsearch.util.sharedData
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : BaseFragment(R.layout.fragment_search, true) {

    override var _binding: ViewDataBinding? = null
    private val binding get() = _binding!! as FragmentSearchBinding

    override val mViewModel by viewModels<SearchViewModel>()

    /** Last query in search view in SearchFragment */
    private var searchViewQuery: String
        get() = sharedData.getSharedString(QUERY).notNull()
        set(value) = sharedData.putSharedString(QUERY, value)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        super.onCreateView(inflater, container, savedInstanceState)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        with (binding) {
            viewModel = mViewModel
            lifecycleOwner = viewLifecycleOwner

            // Hides keyboard on click outside it and clears focuses of editTexts
            searchFragmentLayout.setOnClickListener {
                searchView.clearFocus()
                mViewModel.hideKeyboard(root.windowToken)
            }

            if (searchViewQuery.isNotBlank()) {
                searchView.setQuery(searchViewQuery, false)
                mViewModel.onSearch(searchViewQuery, root.windowToken, false)
            }

            // Search button on keyboard
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    onManualSearch()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    searchViewQuery = newText.notNull()
                    mViewModel.clearScreen()
                    return true
                }
            })

            // Search icon near search box
            searchView.setOnClickListener {
                Timber.d("Search icon clicked")
                onManualSearch()
            }
        }

        return binding.root
    }

    private fun onManualSearch() {
        onClearFocusesAndHideKeyboard()
        mViewModel.onSearch(searchViewQuery, binding.root.windowToken, true)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
