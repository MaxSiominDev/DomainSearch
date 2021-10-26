package dev.maxsiomin.domainsearch.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.domainsearch.util.UiActions
import javax.inject.Inject

/**
 * All ViewModels in project must extend this class
 * @see [UiActions]
 */
@HiltViewModel
open class BaseViewModel @Inject constructor(uiActions: UiActions) : ViewModel(), UiActions by uiActions

fun stringMutableLiveData(value: String? = null): MutableLiveData<String> {
    return MutableLiveData<String>().apply {
        if (value != null)
            this.value = value
    }
}
