package me.vanjavk.isa_shows_app_vanjavk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import me.vanjavk.isa_shows_app_vanjavk.databinding.DialogAddReviewBinding
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentShowDetailsBinding
import me.vanjavk.isa_shows_app_vanjavk.model.Review
import me.vanjavk.isa_shows_app_vanjavk.model.Show
import me.vanjavk.isa_shows_app_vanjavk.viewmodel.ShowDetailsViewModel
import me.vanjavk.isa_shows_app_vanjavk.viewmodel.ShowsViewModel


class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null

    private val binding get() = _binding!!

    private val args: ShowDetailsFragmentArgs by navArgs()

    private var reviewsAdapter: ReviewsAdapter? = null

    private val showsViewModel: ShowsViewModel by navGraphViewModels(R.id.main)

    private val showDetailsViewModel: ShowDetailsViewModel by navGraphViewModels(R.id.main)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        val id = args.showID

        showDetailsViewModel.initShow(showsViewModel.getShows(), id)

        showDetailsViewModel.getShowLiveData().observe(viewLifecycleOwner, { show ->
            updateViews(show)
        })

        val show = showDetailsViewModel.getShowLiveData().value ?: run {
            activity.onBackPressed()
            return
        }

        binding.toolbarLayout.title = show.title
        binding.showImage.setImageResource(show.imageResourceId)
        binding.showDescription.text = show.description

        initWriteReviewButton()
        initReviewsRecycler()
    }


    private fun updateViews(show: Show) {

        refreshReviews(show)

        reviewsAdapter?.setItems(show.reviews)
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
        showsViewModel.updateShow(showDetailsViewModel.addReview(review))
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
    }


    private fun refreshReviews(show: Show) {
        if (show.reviews.isEmpty()) {
            binding.reviewsRecyclerView.visibility = View.GONE
            binding.showReviewRating.visibility = View.GONE
            binding.showRatingBar.visibility = View.GONE
            binding.noReviewsYet.visibility = View.VISIBLE
        } else {
            val averageRating = show.reviews.sumOf { it.stars } / show.reviews.count().toFloat()
            binding.reviewsRecyclerView.visibility = View.VISIBLE
            binding.showReviewRating.text =
                "${show.reviews.count()} REVIEWS, ${"%.2f".format(averageRating)} AVERAGE"
            binding.showReviewRating.visibility = View.VISIBLE
            binding.showRatingBar.rating = averageRating
            binding.showRatingBar.visibility = View.VISIBLE
            binding.noReviewsYet.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                ShowDetailsFragmentDirections.actionBackToShows()
                    .let { findNavController().navigate(it) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

