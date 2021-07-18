package me.vanjavk.isa_shows_app_vanjavk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import me.vanjavk.isa_shows_app_vanjavk.databinding.ActivityShowsBinding
import me.vanjavk.isa_shows_app_vanjavk.model.Genre
import me.vanjavk.isa_shows_app_vanjavk.model.Show

class ShowsActivity : AppCompatActivity() {

    companion object {

        fun buildIntent(activity: Activity): Intent {
            val intent = Intent(activity, ShowsActivity::class.java)
            return intent
        }
    }



    private lateinit var binding: ActivityShowsBinding

    private var showsAdapter: ShowsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initShowsRecycler()

        initAddShowHideButton()
    }

    private fun initAddShowHideButton() {
        binding.showHideButton.setOnClickListener {
            binding.showsRecyclerView.apply {
                isVisible = !isVisible
            }
        }
    }

    private fun initShowsRecycler() {
        showsAdapter = ShowsAdapter(emptyList()) { item ->
            startActivity(ShowsDetailsActivity.buildIntent(this, item.ID))
        }

        binding.showsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.showsRecyclerView.adapter = showsAdapter

        showsAdapter?.setItems(shows)
    }

}