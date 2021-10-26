package dev.maxsiomin.domainsearch.fragments.resetpassword

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.domainsearch.R
import dev.maxsiomin.domainsearch.base.BaseViewModel
import dev.maxsiomin.domainsearch.util.Email
import dev.maxsiomin.domainsearch.util.UiActions
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(uiActions: UiActions) : BaseViewModel(uiActions) {

    fun sendPasswordResetEmail(email: Email, auth: FirebaseAuth) {
        auth.sendPasswordResetEmail(email.value).addOnCompleteListener { task ->
            toast(
                if (task.isSuccessful) R.string.check_email else R.string.unable_to_send_reset_email,
                Toast.LENGTH_LONG,
            )
        }
    }
}
