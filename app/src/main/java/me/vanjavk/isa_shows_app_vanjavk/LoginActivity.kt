package me.vanjavk.isa_shows_app_vanjavk


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import me.vanjavk.isa_shows_app_vanjavk.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(){
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLoginButton()
    }

    private fun initLoginButton() {
        binding.emailInput.doAfterTextChanged {
            checkInputsValid()
        }
        binding.passwordInput.doAfterTextChanged {
            checkInputsValid()
        }

        binding.loginButton.setOnClickListener {
            val intent = WelcomeActivity.buildIntent(this, binding.emailInput.text.toString())
            startActivity(intent)
        }
    }

    private fun checkInputsValid() {
        binding.loginButton.isEnabled = false
        if (binding.emailInput.text.toString().isValidEmail()){
            return
        }
        if (binding.passwordInput.text.toString().length < MIN_PASSWORD_LENGTH){
            return
        }

        binding.loginButton.isEnabled = true
    }
}