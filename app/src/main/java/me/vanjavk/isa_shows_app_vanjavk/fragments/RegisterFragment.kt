package me.vanjavk.isa_shows_app_vanjavk.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import me.vanjavk.isa_shows_app_vanjavk.MIN_PASSWORD_LENGTH
import me.vanjavk.isa_shows_app_vanjavk.R
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentRegisterBinding
import me.vanjavk.isa_shows_app_vanjavk.isValidEmail
import me.vanjavk.isa_shows_app_vanjavk.networking.Status
import me.vanjavk.isa_shows_app_vanjavk.repository.LoginRepository
import me.vanjavk.isa_shows_app_vanjavk.repository.RegistrationRepository
import me.vanjavk.isa_shows_app_vanjavk.viewmodels.RegistrationViewModel
import me.vanjavk.isa_shows_app_vanjavk.viewmodels.ViewModelFactory

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    private val registrationViewModel: RegistrationViewModel by viewModels {
        ViewModelFactory(
            repository = RegistrationRepository(requireActivity())
        )
    }

    private var emailValid = false
    private var passwordValid = false
    private var passwordConfirmationValid = false
    private var registrationInProcess = false
    private var snackbar : Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registrationViewModel.getRegistrationResultLiveData()
            .observe(this.viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        registrationInProcess = true
                        snackbar = Snackbar.make(view, getString(R.string.registering), Snackbar.LENGTH_SHORT)
                        snackbar?.show()
                    }
                    Status.SUCCESS -> {
                        registrationInProcess = false
                        snackbar?.dismiss()
                        RegisterFragmentDirections.actionRegisterToLogin()
                            .apply {
                                this.email = binding.emailInput.text.toString()
                            }
                            .let { findNavController().navigate(it) }
                    }
                    Status.ERROR -> {
                        registrationInProcess = false
                        if (resource.data == true) {
                            snackbar?.dismiss()
                            binding.emailInputLayout.error =
                                getString(R.string.error_email_already_in_use)

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
                checkRegisterButtonEnableable()
            }

        initRegisterButton()
    }

    private fun initRegisterButton() {
        binding.emailInput.doAfterTextChanged {
            checkEmailValid()
            checkRegisterButtonEnableable()
        }
        binding.passwordInput.doAfterTextChanged {
            checkPasswordValid()
            checkPasswordConfirmationValid()
            checkRegisterButtonEnableable()
        }
        binding.passwordConfirmationInput.doAfterTextChanged {
            checkPasswordConfirmationValid()
            checkRegisterButtonEnableable()
        }

        binding.registerButton.setOnClickListener {
            registrationInProcess = true
            checkRegisterButtonEnableable()
            registrationViewModel.register(
                binding.emailInput.text.toString(),
                binding.passwordInput.text.toString()
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

    private fun checkPasswordConfirmationValid() {
        if (binding.passwordConfirmationInput.text.toString().isEmpty()) {
            return
        }

        if (binding.passwordInput.text.toString() != binding.passwordConfirmationInput.text.toString()) {
            binding.passwordConfirmationInputLayout.error =
                getString(R.string.passwords_do_not_match)
            passwordConfirmationValid = false
        } else {
            binding.passwordConfirmationInputLayout.error = null
            passwordConfirmationValid = true
        }
    }

    private fun checkRegisterButtonEnableable() {
        binding.registerButton.isEnabled =
            passwordValid && emailValid && passwordConfirmationValid && !registrationInProcess
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}