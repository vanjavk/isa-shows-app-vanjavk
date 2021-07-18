package me.vanjavk.isa_shows_app_vanjavk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.vanjavk.isa_shows_app_vanjavk.databinding.ActivityShowDetailsBinding

class ShowsDetailsActivity : AppCompatActivity() {

    companion object {

        fun buildIntent(activity: Activity): Intent {
            val intent = Intent(activity, ShowsDetailsActivity::class.java)
            return intent
        }
    }

    private lateinit var binding: ActivityShowDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}