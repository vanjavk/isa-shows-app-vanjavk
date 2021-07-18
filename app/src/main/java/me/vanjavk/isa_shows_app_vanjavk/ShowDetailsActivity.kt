package me.vanjavk.isa_shows_app_vanjavk

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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

    private var reviewsAdapter: ReviewsAdapter? = null

    private lateinit var show: Show

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowDetailsBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val ID = intent.extras?.get(EXTRA_ID)
        show = shows.find { it.ID == ID }!!

        binding.toolbarLayout.title = show.title

        //binding.showTitle.text = show.title
        binding.showImage.setImageResource(show.imageResourceId)
        binding.showDescription.text = show.description

        setContentView(binding.root)

        initReviewsRecycler()
    }

    private fun initReviewsRecycler() {

        reviewsAdapter = ReviewsAdapter(emptyList())

        binding.reviewsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.reviewsRecyclerView.adapter = reviewsAdapter
        if (show.reviews.isEmpty()) {
            binding.reviewsRecyclerView.visibility = GONE
            binding.showReviewRating.visibility = GONE
            binding.showRatingBar.visibility = GONE
            binding.noReviewsYet.visibility = VISIBLE
        } else {
            val averageRating = show.reviews.sumOf { it.stars } / show.reviews.count().toFloat()
            binding.reviewsRecyclerView.visibility = VISIBLE
            binding.showReviewRating.text =
                "${show.reviews.count()} REVIEWS, ${averageRating} AVERAGE"
            binding.showReviewRating.visibility = VISIBLE
            binding.showRatingBar.rating = averageRating
            binding.showRatingBar.visibility = VISIBLE
            binding.noReviewsYet.visibility = GONE
            reviewsAdapter?.setItems(show.reviews)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}