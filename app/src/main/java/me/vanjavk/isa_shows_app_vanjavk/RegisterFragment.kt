package me.vanjavk.isa_shows_app_vanjavk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentRegisterBinding
import me.vanjavk.isa_shows_app_vanjavk.viewmodel.RegistrationViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    private val registrationViewModel: RegistrationViewModel by viewModels()

    private var emailValid = false
    private var passwordValid = false
    private var passwordConfirmationValid = false
    private var registrationInProcess = false

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
            .observe(this.viewLifecycleOwner) { isRegisterSuccessful ->
                if (isRegisterSuccessful) {
                    RegisterFragmentDirections.actionRegisterToLogin().apply {
                        this.email = binding.emailInput.text.toString()
                    }
                        .let { findNavController().navigate(it) }

                } else {
                    Toast.makeText(activity, "Registration unsuccessful!", Toast.LENGTH_SHORT)
                        .show()
                    registrationInProcess = false
                    checkRegisterButtonEnableable()
                }
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
            binding.emailInputLayout.error = "Invalid email!"
            emailValid = false
        } else {
            binding.emailInputLayout.error = null
            emailValid = true
        }
    }

    private fun checkPasswordValid() {
        if (binding.passwordInput.text.toString().length < MIN_PASSWORD_LENGTH) {
            binding.passwordInputLayout.error = "Password too short"
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
            binding.passwordConfirmationInputLayout.error = getString(R.string.passwords_do_not_match)
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