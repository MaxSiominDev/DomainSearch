package dev.maxsiomin.domainsearch.activities.login

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.domainsearch.R
import dev.maxsiomin.domainsearch.activities.main.MainActivity
import dev.maxsiomin.domainsearch.base.DialogBuilder
import dev.maxsiomin.domainsearch.fragments.contract.Navigator
import dev.maxsiomin.domainsearch.fragments.login.LoginFragment
import dev.maxsiomin.domainsearch.util.SHARED_DATA
import dev.maxsiomin.domainsearch.util.SharedData
import dev.maxsiomin.domainsearch.util.SharedDataImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * The only activity in application that doesn't extend BaseActivity
 * Contains 3 Fragments: LoginFragment, SignupFragment, ResetPasswordFragment
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), Navigator {

    lateinit var sharedData: SharedData

    private lateinit var analytics: FirebaseAnalytics

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        analytics = Firebase.analytics

        sharedData = SharedDataImpl(savedInstanceState?.getBundle(SHARED_DATA))

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.login_activity_fragment_container, LoginFragment.newInstance())
                .commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        Timber.d("onSaveInstanceState called")
        outState.putBundle(SHARED_DATA, sharedData.toBundle())
        super.onSaveInstanceState(outState, outPersistentState)
    }

    /**
     * Called from LoginFragment or from [VerifyEmailDialog] when user just logged in
     */
    fun onLogin() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun launchFragment(container: Int, fragment: Fragment, addToBackStack: Boolean) {
        with (supportFragmentManager.beginTransaction()) {
            replace(container, fragment)

            if (addToBackStack)
                addToBackStack(null)

            commit()
        }
    }

    override fun goBack() {
        supportFragmentManager.popBackStack()
    }

    /**
     * Called from SignupFragment when user just signed up
     */
    fun onSignup() {
        val dialog = VerifyEmailDialog.newInstance()
        dialog.show(supportFragmentManager)
    }

    /**
     * Suggest user to verify email. It's not mandatory
     */
    class VerifyEmailDialog : DialogFragment() {

        private val loginActivity get() = requireActivity() as LoginActivity

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            val dialog = DialogBuilder(requireContext())
                .setMessage(R.string.want_verify_email)
                .setNegativeButton(R.string.no_thanks) { _, _ ->
                    loginActivity.onLogin()
                }
                .setPositiveButton(R.string.verify) { _, _ ->
                    loginActivity.auth.currentUser!!.sendEmailVerification()
                    loginActivity.onLogin()
                }
                .create()

            dialog.setCanceledOnTouchOutside(false)

            return dialog
        }

        fun show(manager: FragmentManager) {
            show(manager, TAG)
        }

        companion object {

            const val TAG = "VerifyEmailDialog"

            @JvmStatic
            fun newInstance() = VerifyEmailDialog()
        }
    }
}
