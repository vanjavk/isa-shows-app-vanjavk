package me.vanjavk.isa_shows_app_vanjavk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private var emailValid = false
    private var passwordValid = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLoginButton()
    }

    private fun initLoginButton() {
        binding.emailInput.doAfterTextChanged {
            checkEmailValid()
            checkInputsValid()
        }
        binding.passwordInput.doAfterTextChanged {
            checkPasswordValid()
            checkInputsValid()
        }

        binding.loginButton.setOnClickListener {
            LoginFragmentDirections.actionLoginToShows(binding.emailInput.text.toString())
                .let { findNavController().navigate(it) }

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

    private fun checkInputsValid() {
        binding.loginButton.isEnabled = passwordValid && emailValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}