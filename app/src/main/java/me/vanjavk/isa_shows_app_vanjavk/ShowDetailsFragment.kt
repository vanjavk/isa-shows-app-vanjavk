package me.vanjavk.isa_shows_app_vanjavk

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import me.vanjavk.isa_shows_app_vanjavk.databinding.DialogAddReviewBinding
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentShowDetailsBinding
import me.vanjavk.isa_shows_app_vanjavk.model.Review
import me.vanjavk.isa_shows_app_vanjavk.model.Show


class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null

    private val binding get() = _binding!!

    private val args: ShowDetailsFragmentArgs by navArgs()

    private var reviewsAdapter: ReviewsAdapter? = null

    private lateinit var show: Show

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as AppCompatActivity

        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val id = args.showID
        val tempShow = shows.find { it.ID == id }
        if (tempShow == null) {
            Toast.makeText(
                activity,
                "Show with specified ID could not be found.",
                Toast.LENGTH_SHORT
            )
                .show()
            activity.onBackPressed()
        } else {
            show = tempShow
        }

        binding.toolbarLayout.title = show.title

        binding.showImage.setImageResource(show.imageResourceId)
        binding.showDescription.text = show.description

        initWriteReviewButton()
        initReviewsRecycler()
    }

    private fun initWriteReviewButton() {
        binding.writeReviewButton.setOnClickListener {
            showAddReviewBottomSheet()
        }
    }

    private fun showAddReviewBottomSheet() {

        val activity = activity as AppCompatActivity

        val dialog = BottomSheetDialog(activity)

        val bottomSheetBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.starRatingBar.setOnRatingBarChangeListener { _: RatingBar, _: Float, _: Boolean ->
            bottomSheetBinding.confirmButton.isEnabled = true
        }

        bottomSheetBinding.confirmButton.setOnClickListener {
            val review = Review(
                args.email.getUsername(),
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

        binding.reviewsRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.reviewsRecyclerView.adapter = reviewsAdapter
        binding.reviewsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )

        refreshReviews()

        reviewsAdapter?.setItems(show.reviews)
    }


    private fun refreshReviews() {
        binding.reviewsRecyclerView.isVisible = show.reviews.isNotEmpty()
        binding.showReviewRating.isVisible = show.reviews.isNotEmpty()
        binding.showRatingBar.isVisible = show.reviews.isNotEmpty()
        binding.noReviewsYet.isVisible = show.reviews.isEmpty()
        if (show.reviews.isNotEmpty()) {
            val averageRating = show.reviews.map { it.stars }.average().toFloat()
            binding.showReviewRating.text =
                "${show.reviews.count()} REVIEWS, ${"%.2f".format(averageRating)} AVERAGE"
            binding.showRatingBar.rating = averageRating
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

