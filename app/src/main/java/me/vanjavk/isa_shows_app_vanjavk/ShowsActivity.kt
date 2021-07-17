package me.vanjavk.isa_shows_app_vanjavk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.vanjavk.isa_shows_app_vanjavk.databinding.ActivityShowsBinding

class ShowsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowsBinding

    companion object {

        fun buildIntent(activity: Activity): Intent {
            val intent = Intent(activity, ShowsActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}