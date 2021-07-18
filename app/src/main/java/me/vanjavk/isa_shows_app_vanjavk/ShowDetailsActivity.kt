package me.vanjavk.isa_shows_app_vanjavk

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RatingBar
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

        val review = Review("hardkodiranoimeaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
            bottomSheetBinding.commentInput.text.toString(), bottomSheetBinding.starRatingBar.numStars)

        bottomSheetBinding.starRatingBar.setOnRatingBarChangeListener { _: RatingBar, _: Float, _: Boolean -> bottomSheetBinding.confirmButton.isEnabled = true
        }

        bottomSheetBinding.confirmButton.setOnClickListener {
            addReviewToList(review)
            dialog.dismiss()
        }

        dialog.show()

    }

    private fun addReviewToList(review: Review) {
        show.reviews.add(review)
        reviewsAdapter?.addItem(review)
        refreshReviews()
    }

    private fun initReviewsRecycler() {

        reviewsAdapter = ReviewsAdapter(emptyList())

        binding.reviewsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.reviewsRecyclerView.adapter = reviewsAdapter
        binding.reviewsRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

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
            val averageRating = show.reviews.sumOf { it.stars } / show.reviews.count().toFloat()
            binding.reviewsRecyclerView.visibility = VISIBLE
            binding.showReviewRating.text =
                "${show.reviews.count()} REVIEWS, ${averageRating} AVERAGE"
            binding.showReviewRating.visibility = VISIBLE
            binding.showRatingBar.rating = averageRating
            binding.showRatingBar.visibility = VISIBLE
            binding.noReviewsYet.visibility = GONE
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