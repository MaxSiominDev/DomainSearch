package dev.maxsiomin.domainsearch.fragments.login

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.domainsearch.R
import dev.maxsiomin.domainsearch.base.BaseViewModel
import dev.maxsiomin.domainsearch.util.Email
import dev.maxsiomin.domainsearch.util.Password
import dev.maxsiomin.domainsearch.util.UiActions
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(uiActions: UiActions) : BaseViewModel(uiActions) {

    /**
     * Login via [FirebaseAuth]
     */
    fun login(email: Email, password: Password, auth: FirebaseAuth, onLogin: () -> Unit) {
        auth.signInWithEmailAndPassword(email.value, password.value).addOnCompleteListener { task ->
            if (task.isSuccessful)
                onLogin()
            else
                toast(R.string.unable_to_login, Toast.LENGTH_LONG)
        }
    }
}
