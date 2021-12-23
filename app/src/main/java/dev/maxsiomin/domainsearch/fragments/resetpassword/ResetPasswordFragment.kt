package dev.maxsiomin.domainsearch.fragments.resetpassword

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.domainsearch.R
import dev.maxsiomin.domainsearch.base.BaseFragment
import dev.maxsiomin.domainsearch.databinding.FragmentResetPasswordBinding
import dev.maxsiomin.domainsearch.extensions.clearError
import dev.maxsiomin.domainsearch.util.Email

@AndroidEntryPoint
class ResetPasswordFragment : BaseFragment(R.layout.fragment_reset_password) {

    override var _binding: ViewBinding? = null
    private val binding get() = _binding as FragmentResetPasswordBinding

    override val mViewModel by viewModels<ResetPasswordViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentResetPasswordBinding.bind(view)

        with (binding) {
            resetPasswordButton.setOnClickListener {
                val email = Email(emailEditText.text)

                if (!email.isCorrect) {
                    emailEditTextLayout.error = getString(R.string.invalid_email)
                    emailEditText.requestFocus()
                } else {
                    mViewModel.sendPasswordResetEmail(email, FirebaseAuth.getInstance())
                }
            }

            emailEditText.addTextChangedListener {
                emailEditTextLayout.clearError()
            }
        }
    }
}
