package me.vanjavk.isa_shows_app_vanjavk.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import me.vanjavk.isa_shows_app_vanjavk.*
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentLoginBinding
import me.vanjavk.isa_shows_app_vanjavk.networking.Status
import me.vanjavk.isa_shows_app_vanjavk.repository.LoginRepository
import me.vanjavk.isa_shows_app_vanjavk.repository.ShowsRepository
import me.vanjavk.isa_shows_app_vanjavk.viewmodels.LoginViewModel
import me.vanjavk.isa_shows_app_vanjavk.viewmodels.ViewModelFactory

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val args: LoginFragmentArgs by navArgs()

    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory(
            requireActivity().getPreferences(Context.MODE_PRIVATE),
            LoginRepository(requireActivity())
        )
    }

    private var emailValid = false
    private var passwordValid = false
    private var loginInProcess = false
    private var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)

        val loggedIn = sharedPref.getBoolean(REMEMBER_ME_KEY, false)
        if (loggedIn) {
            LoginFragmentDirections.actionLoginToShows()
                .let { findNavController().navigate(it) }
        }

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
            .observe(this.viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        loginInProcess = true
                        snackbar = Snackbar.make(
                            view,
                            getString(R.string.logging_in),
                            Snackbar.LENGTH_SHORT
                        )
                        snackbar?.show()
                    }
                    Status.SUCCESS -> {
                        loginInProcess = false
                        snackbar?.dismiss()
                        LoginFragmentDirections.actionLoginToShows()
                            .let { findNavController().navigate(it) }
                    }
                    Status.ERROR -> {
                        loginInProcess = false
                        if (resource.data == true) {
                            binding.passwordInputLayout.error =
                                getString(R.string.error_email_and_password_combination)
                            snackbar?.dismiss()
                        } else {
                            snackbar = Snackbar.make(
                                view,
                                getString(R.string.error_no_internet),
                                Snackbar.LENGTH_LONG
                            )
                            snackbar?.show()
                        }

                    }
                }
                checkLoginButtonEnableable()
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
        checkEmailValid()
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