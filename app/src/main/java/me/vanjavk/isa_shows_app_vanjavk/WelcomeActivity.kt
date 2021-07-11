package me.vanjavk.isa_shows_app_vanjavk
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import me.vanjavk.isa_shows_app_vanjavk.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity(){



    private lateinit var binding: ActivityWelcomeBinding

    companion object {
        private const val EXTRA_EMAIL = "EXTRA_EMAIL"

        fun buildIntent(activity: Activity, email: String) : Intent {
            val intent = Intent(activity, WelcomeActivity::class.java)
            intent.putExtra(EXTRA_EMAIL, email)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.extras?.getString(EXTRA_EMAIL)
        val username = email!!.substring(0, email!!.indexOf('@'))

        binding.welcomeText.text="Welcome, $username!"


    }
}