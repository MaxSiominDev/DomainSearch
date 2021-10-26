package dev.maxsiomin.domainsearch.fragments.signup

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
class SignupViewModel @Inject constructor(uiActions: UiActions) : BaseViewModel(uiActions) {

    fun signup(email: Email, password: Password, auth: FirebaseAuth, onSignup: () -> Unit) {
        auth.createUserWithEmailAndPassword(email.value, password.value).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSignup()
            } else {
                toast(R.string.unable_to_signup, Toast.LENGTH_LONG)
            }
        }
    }
}
