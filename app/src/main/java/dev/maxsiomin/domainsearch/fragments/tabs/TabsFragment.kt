package dev.maxsiomin.domainsearch.fragments.tabs

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.domainsearch.R
import dev.maxsiomin.domainsearch.base.BaseFragment
import dev.maxsiomin.domainsearch.databinding.FragmentTabsBinding

@AndroidEntryPoint
class TabsFragment : BaseFragment(R.layout.fragment_tabs) {

    override var _binding: ViewBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentTabsBinding.bind(view)

        with (childFragmentManager.findFragmentById(R.id.tabsContainer) as NavHostFragment) {
            NavigationUI.setupWithNavController((_binding as FragmentTabsBinding).bottomNavigationView, navController)
        }
    }
}
