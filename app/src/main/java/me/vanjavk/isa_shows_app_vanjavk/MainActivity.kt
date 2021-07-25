package me.vanjavk.isa_shows_app_vanjavk

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import me.vanjavk.isa_shows_app_vanjavk.databinding.ActivityMainBinding
import me.vanjavk.isa_shows_app_vanjavk.networking.ApiModule
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ApiModule.initRetrofit(getPreferences(Context.MODE_PRIVATE))
    }
}