package dev.maxsiomin.domainsearch.fragments.login

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.domainsearch.R
import dev.maxsiomin.domainsearch.base.BaseFragment
import dev.maxsiomin.domainsearch.databinding.FragmentLoginBinding
import dev.maxsiomin.domainsearch.extensions.clearError
import dev.maxsiomin.domainsearch.util.Email
import dev.maxsiomin.domainsearch.util.Password
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login) {

    override var _binding: ViewBinding? = null
    private val binding get() = _binding as FragmentLoginBinding

    override val mViewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.d("onViewCreated called")

        if (auth.currentUser != null)
            findNavController().navigate(R.id.action_loginFragment_to_tabsFragment)

        _binding = FragmentLoginBinding.bind(view)

        with (binding) {

            loginButton.setOnClickListener {
                val email = Email(emailEditText.text)
                val password = Password(passwordEditText.text)

                when {
                    !email.isCorrect -> {
                        emailEditTextLayout.error = getString(R.string.invalid_email)
                        emailEditText.requestFocus()
                    }

                    password.isEmpty -> {
                        passwordEditTextLayout.error = getString(R.string.password_not_provided)
                        passwordEditText.requestFocus()
                    }

                    else -> {
                        mViewModel.login(email, password, auth) {
                            onLogin()
                        }
                    }
                }
            }

            emailEditText.addTextChangedListener { emailEditTextLayout.clearError() }
            passwordEditText.addTextChangedListener { passwordEditTextLayout.clearError() }

            forgotPasswordTextView.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
            }

            signupTextView.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
            }
        }
    }

    private fun onLogin() {
        findNavController().navigate(R.id.action_loginFragment_to_tabsFragment)
    }
}
