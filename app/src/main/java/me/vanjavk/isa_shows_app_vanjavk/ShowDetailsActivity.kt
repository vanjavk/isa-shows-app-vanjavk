package me.vanjavk.isa_shows_app_vanjavk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.vanjavk.isa_shows_app_vanjavk.databinding.ActivityShowDetailsBinding
import me.vanjavk.isa_shows_app_vanjavk.model.Show

class ShowsDetailsActivity : AppCompatActivity() {

    companion object {
        fun buildIntent(activity: Activity, ID: String): Intent {
            val intent = Intent(activity, ShowsDetailsActivity::class.java)
            intent.putExtra(EXTRA_ID, ID)
            return intent
        }
    }

    private lateinit var binding: ActivityShowDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowDetailsBinding.inflate(layoutInflater)

        val ID = intent.extras?.get(EXTRA_ID)
        val show = shows.find { it.ID == ID }!!

        binding.showTitle.text = show.title
        binding.showImage.setImageResource(show.imageResourceId)
        binding.showDescription.text = show.description

        setContentView(binding.root)

    }
}