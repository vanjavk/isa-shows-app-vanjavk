package me.vanjavk.isa_shows_app_vanjavk

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import me.vanjavk.isa_shows_app_vanjavk.databinding.ActivityShowDetailsBinding
import me.vanjavk.isa_shows_app_vanjavk.databinding.DialogAddReviewBinding
import me.vanjavk.isa_shows_app_vanjavk.model.Review
import me.vanjavk.isa_shows_app_vanjavk.model.Show

class ShowsDetailsActivity : AppCompatActivity() {

    companion object {
        fun buildIntent(activity: Activity, ID: String): Intent {
            val intent = Intent(activity, ShowsDetailsActivity::class.java)
            intent.putExtra(EXTRA_ID, ID)
            intent.putExtra(EXTRA_EMAIL, ID)
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

        val id = intent.extras?.get(EXTRA_ID)
        val tempShow = shows.find { it.ID == id }
        if (tempShow == null) {
            Toast.makeText(this, "Show with specified ID could not be found.", Toast.LENGTH_SHORT)
                .show()
            this.onBackPressed()
        } else {
            show = tempShow
        }

        binding.toolbarLayout.title = show.title

        binding.showImage.setImageResource(show.imageResourceId)
        binding.showDescription.text = show.description

        setContentView(binding.root)

        initWriteReviewButton()
        initReviewsRecycler()
    }

    private fun initWriteReviewButton() {
        binding.writeReviewButton.setOnClickListener {
            showAddReviewBottomSheet()
        }
    }

    private fun showAddReviewBottomSheet() {

        val dialog = BottomSheetDialog(this)

        val bottomSheetBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.starRatingBar.setOnRatingBarChangeListener { _: RatingBar, _: Float, _: Boolean ->
            bottomSheetBinding.confirmButton.isEnabled = true
        }

        val email = intent.extras?.get(EXTRA_EMAIL).toString()
        bottomSheetBinding.confirmButton.setOnClickListener {
            val review = Review(
                email,
                bottomSheetBinding.commentInput.text.toString(),
                bottomSheetBinding.starRatingBar.rating.toInt()
            )
            addReviewToList(review)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun addReviewToList(review: Review) {
        reviewsAdapter?.addItem(review)
        show.reviews.add(review)
        refreshReviews()
    }

    private fun initReviewsRecycler() {

        reviewsAdapter = ReviewsAdapter(emptyList())

        binding.reviewsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.reviewsRecyclerView.adapter = reviewsAdapter
        binding.reviewsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        refreshReviews()

        reviewsAdapter?.setItems(show.reviews)
    }


    private fun refreshReviews() {
        if (show.reviews.isEmpty()) {
            binding.reviewsRecyclerView.visibility = GONE
            binding.showReviewRating.visibility = GONE
            binding.showRatingBar.visibility = GONE
            binding.noReviewsYet.visibility = VISIBLE
        } else {
            val averageRating = show.reviews.map { it.stars }.average().toFloat()
            binding.reviewsRecyclerView.visibility = VISIBLE
            binding.showReviewRating.text =
                "${show.reviews.count()} REVIEWS, ${"%.2f".format(averageRating)} AVERAGE"
            binding.showReviewRating.visibility = VISIBLE
            binding.showRatingBar.rating = averageRating
            binding.showRatingBar.visibility = VISIBLE
            binding.noReviewsYet.visibility = GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}