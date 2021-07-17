package me.vanjavk.isa_shows_app_vanjavk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import me.vanjavk.isa_shows_app_vanjavk.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private var emailValid = false
    private var passwordValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            val intent = WelcomeActivity.buildIntent(binding.emailInput.text.toString())
            startActivity(intent)
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

    private fun checkInputsValid() {
        binding.loginButton.isEnabled = passwordValid && emailValid
    }
}