package me.vanjavk.isa_shows_app_vanjavk

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import me.vanjavk.isa_shows_app_vanjavk.databinding.DialogAddReviewBinding
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentShowDetailsBinding
import me.vanjavk.isa_shows_app_vanjavk.model.RatingInfo
import me.vanjavk.isa_shows_app_vanjavk.model.Review
import me.vanjavk.isa_shows_app_vanjavk.model.Show
import me.vanjavk.isa_shows_app_vanjavk.viewmodel.ShowDetailsViewModel
import me.vanjavk.isa_shows_app_vanjavk.viewmodel.ShowsViewModel


class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null

    private val binding get() = _binding!!

    private val args: ShowDetailsFragmentArgs by navArgs()

    private var reviewsAdapter: ReviewsAdapter? = null

    private val showDetailsViewModel: ShowDetailsViewModel by viewModels()

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
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val id = args.showID

        showDetailsViewModel.initShow(id)

        val show = showDetailsViewModel.getShowLiveData().value
        if (show == null) {
            Toast.makeText(
                activity,
                getString(R.string.error_show_id_not_found),
                Toast.LENGTH_SHORT
            ).show()
            activity.onBackPressed()
            return
        }

        showDetailsViewModel.getShowLiveData().observe(viewLifecycleOwner, { show ->
            updateShow(show)
            updateReviews(show.reviews)
        })

        showDetailsViewModel.getReviewsLiveData().observe(viewLifecycleOwner, { reviews ->
            updateReviews(reviews.last())
        })

        showDetailsViewModel.getRatingInfoLiveData().observe(viewLifecycleOwner, { ratingInfo ->
            updateRatingInfo(ratingInfo)
        })

        initWriteReviewButton()
        initReviewsRecycler()
    }

    private fun updateShow(show: Show) {

    }

    private fun updateRatingInfo(ratingInfo: RatingInfo) {
        if (ratingInfo.numberOfReviews == 0) {
            binding.reviewsRecyclerView.visibility = View.GONE
            binding.showReviewRating.visibility = View.GONE
            binding.showRatingBar.visibility = View.GONE
            binding.noReviewsYet.visibility = View.VISIBLE
        } else {
            binding.noReviewsYet.visibility = View.GONE
            binding.showReviewRating.visibility = View.VISIBLE
            binding.showRatingBar.visibility = View.VISIBLE
            binding.showReviewRating.text = getString(R.string.reviews_rating_info).format(
                ratingInfo.numberOfReviews,
                ratingInfo.averageRating
            )
            binding.showRatingBar.rating = ratingInfo.averageRating
            binding.reviewsRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun updateReviews(reviews: List<Review>) {
        reviewsAdapter?.setItems(reviews)
    }

    private fun updateReviews(review: Review) {
        reviewsAdapter?.addItem(review)
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
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
            if (sharedPref == null) {
                Toast.makeText(activity, "Action failed. Aborting...", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val email =
                sharedPref.getString(getString(R.string.user_email_key), "Default_user").orEmpty()

            showDetailsViewModel.addReview(
                email.getUsername(),
                bottomSheetBinding.commentInput.text.toString(),
                bottomSheetBinding.starRatingBar.rating.toInt()
            )
            dialog.dismiss()
        }
        dialog.show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

