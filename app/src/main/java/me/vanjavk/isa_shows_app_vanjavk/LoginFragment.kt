package me.vanjavk.isa_shows_app_vanjavk

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentLoginBinding
import me.vanjavk.isa_shows_app_vanjavk.viewmodel.LoginViewModel
import me.vanjavk.isa_shows_app_vanjavk.viewmodel.LoginViewModelFactory

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val args: LoginFragmentArgs by navArgs()

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginViewModelFactory: LoginViewModelFactory

    private var emailValid = false
    private var passwordValid = false
    private var loginInProcess = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        if (sharedPref == null) {
            Toast.makeText(activity, getString(R.string.error_shared_pref_is_null), Toast.LENGTH_SHORT).show()
            return null
        }

        val loggedIn = sharedPref.getBoolean(REMEMBER_ME_KEY, false)
        if (loggedIn) {
            LoginFragmentDirections.actionLoginToShows()
                .let { findNavController().navigate(it) }
        }

        loginViewModelFactory = LoginViewModelFactory(sharedPref)
        loginViewModel = ViewModelProvider(this, loginViewModelFactory)
            .get(LoginViewModel::class.java)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = args.email
        if (email != null) {
            initFromRegister(email)
        }

        loginViewModel.getLoginResultLiveData()
            .observe(this.viewLifecycleOwner) { isLoginSuccessful ->
                if (isLoginSuccessful) {
                    LoginFragmentDirections.actionLoginToShows()
                        .let { findNavController().navigate(it) }
                } else {
                    Toast.makeText(activity, "Email and password combination is incorrect!", Toast.LENGTH_SHORT)
                        .show()
                    loginInProcess = false
                    checkLoginButtonEnableable()
                }
            }

        initLoginButton()
        initRegisterButton()
    }

    private fun initRegisterButton() {
        binding.registerButton.setOnClickListener {
            LoginFragmentDirections.actionLoginToRegister()
                .let { findNavController().navigate(it) }
        }
    }

    private fun initFromRegister(email: String) {
        binding.loginText.text = getString(R.string.registration_successful)
        binding.registerButton.visibility = GONE
        binding.emailInput.setText(email)
    }

    private fun initLoginButton() {
        binding.emailInput.doAfterTextChanged {
            checkEmailValid()
            checkLoginButtonEnableable()
        }
        binding.passwordInput.doAfterTextChanged {
            checkPasswordValid()
            checkLoginButtonEnableable()
        }

        binding.loginButton.setOnClickListener {
            loginInProcess = true
            checkLoginButtonEnableable()
            loginViewModel.login(
                binding.emailInput.text.toString(),
                binding.passwordInput.text.toString(),
                binding.rememberMeCheckBox.isChecked
            )
        }

    }


    private fun checkEmailValid() {
        if (binding.emailInput.text.toString().isValidEmail()) {
            binding.emailInputLayout.error = getString(R.string.invalid_email)
            emailValid = false
        } else {
            binding.emailInputLayout.error = null
            emailValid = true
        }
    }

    private fun checkPasswordValid() {
        if (binding.passwordInput.text.toString().length < MIN_PASSWORD_LENGTH) {
            binding.passwordInputLayout.error = getString(R.string.password_too_short)
            passwordValid = false
        } else {
            binding.passwordInputLayout.error = null
            passwordValid = true
        }
    }

    private fun checkLoginButtonEnableable() {
        binding.loginButton.isEnabled = passwordValid && emailValid && !loginInProcess
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}