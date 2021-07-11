package me.vanjavk.isa_shows_app_vanjavk


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import me.vanjavk.isa_shows_app_vanjavk.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(){
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initButton()

    }

    private fun initButton() {
        binding.emailInput.doAfterTextChanged {
            checkInputsValid()
        }

        binding.loginButton.setOnClickListener {


            if (etLoginUsername.length() < 1) {
                Toast.makeText(this, "Please enter username.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (etLoginPassword.text.toString().length <= 0) {
                Toast.makeText(this, "Please enter password.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val intent = WelcomeActivity.buildIntent(this, binding.emailInput.text.toString())
            startActivity(intent)
        }
    }

    private fun checkInputsValid() {
        binding.loginButton.isEnabled = false
        if (binding.emailInput.text.toString().isValidEmail()){
            return
        }

        binding.loginButton.isEnabled = true
    }
}