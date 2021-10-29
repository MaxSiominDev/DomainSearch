package dev.maxsiomin.domainsearch.fragments.contract

import androidx.fragment.app.Fragment

val Fragment.navigator get() = requireActivity() as Navigator

interface Navigator {

    fun launchFragment(container: Int, fragment: Fragment, addToBackStack: Boolean = true)

    fun goBack()
}
