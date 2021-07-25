package me.vanjavk.isa_shows_app_vanjavk

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    private var emailValid = false
    private var passwordValid = false
    private var passwordConfirmationValid = false

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
        initRegisterButton()
    }

    private fun initRegisterButton() {
        binding.emailInput.doAfterTextChanged {
            checkEmailValid()
            checkInputsValid()
        }
        binding.passwordInput.doAfterTextChanged {
            checkPasswordValid()
            checkPasswordConfirmationValid()
            checkInputsValid()
        }
        binding.passwordConfirmationInput.doAfterTextChanged {
            checkPasswordConfirmationValid()
            checkInputsValid()
        }

        binding.registerButton.setOnClickListener {

            val email = binding.emailInput.text.toString()

            RegisterFragmentDirections.actionRegisterToLogin().apply {
                this.email = email
            }
                .let { findNavController().navigate(it) }
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
        if (binding.passwordConfirmationInput.text.toString().isEmpty()){
            return
        }

        if (binding.passwordInput.text.toString() != binding.passwordConfirmationInput.text.toString()) {
            binding.passwordConfirmationInputLayout.error = "Passwords are not the same"
            passwordConfirmationValid = false
        } else {
            binding.passwordConfirmationInputLayout.error = null
            passwordConfirmationValid = true
        }
    }

    private fun checkInputsValid() {
        binding.registerButton.isEnabled = passwordValid && emailValid && passwordConfirmationValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}