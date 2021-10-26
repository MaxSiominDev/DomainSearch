package dev.maxsiomin.domainsearch.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.domainsearch.R
import dev.maxsiomin.domainsearch.activities.login.LoginActivity
import dev.maxsiomin.domainsearch.base.BaseFragment
import dev.maxsiomin.domainsearch.databinding.FragmentLoginBinding
import dev.maxsiomin.domainsearch.fragments.resetpassword.ResetPasswordFragment
import dev.maxsiomin.domainsearch.fragments.signup.SignupFragment
import dev.maxsiomin.domainsearch.util.*
import dev.maxsiomin.domainsearch.util.SharedDataKeys.EMAIL
import dev.maxsiomin.domainsearch.util.SharedDataKeys.PASSWORD
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login, false) {

    override var _binding: ViewDataBinding? = null
    private val binding get() = _binding!! as FragmentLoginBinding

    override val mViewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var auth: FirebaseAuth

    val loginActivity get() = requireActivity() as LoginActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("onCreate called")

        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        with (binding) {
            emailEditText.text = sharedData.getSharedString(EMAIL).notNull().toEditable()
            passwordEditText.text = sharedData.getSharedString(PASSWORD).notNull().toEditable()

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
                activityFragmentManager.beginTransaction()
                    .replace(R.id.login_activity_fragment_container, ResetPasswordFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }

            signupTextView.setOnClickListener {
                // Do not add to backstack!
                activityFragmentManager.beginTransaction()
                    .replace(R.id.login_activity_fragment_container, SignupFragment.newInstance())
                    .commit()
            }
        }

        return binding.root
    }

    override fun onStop() {
        sharedData.putSharedString(EMAIL, binding.emailEditText.text?.toString())
        sharedData.putSharedString(PASSWORD, binding.passwordEditText.text?.toString())
        super.onStop()
    }

    private fun onLogin() {
        loginActivity.onLogin()
    }

    companion object {

        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}
