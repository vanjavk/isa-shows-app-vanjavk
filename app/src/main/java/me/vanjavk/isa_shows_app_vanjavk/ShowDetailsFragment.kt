package me.vanjavk.isa_shows_app_vanjavk

import Show
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import me.vanjavk.isa_shows_app_vanjavk.adapter.ReviewsAdapter
import me.vanjavk.isa_shows_app_vanjavk.databinding.DialogAddReviewBinding
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentShowDetailsBinding
import me.vanjavk.isa_shows_app_vanjavk.model.Review
import me.vanjavk.isa_shows_app_vanjavk.viewmodel.ShowDetailsViewModel
import me.vanjavk.isa_shows_app_vanjavk.viewmodel.ViewModelFactory


class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null

    private val binding get() = _binding!!

    private val args: ShowDetailsFragmentArgs by navArgs()

    private var reviewsAdapter: ReviewsAdapter? = null

    private lateinit var showDetailsViewModel: ShowDetailsViewModel
    private lateinit var showDetailsViewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true);

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        if (sharedPref == null) {
            Toast.makeText(
                activity,
                getString(R.string.error_shared_pref_is_null),
                Toast.LENGTH_SHORT
            ).show()
            activity?.onBackPressed()
            return binding.root
        }

        showDetailsViewModelFactory = ViewModelFactory(sharedPref)
        showDetailsViewModel = ViewModelProvider(this, showDetailsViewModelFactory)
            .get(ShowDetailsViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as AppCompatActivity

        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbarLayout.title = getString(R.string.loading)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val id = args.showID

        showDetailsViewModel.getShow(id)

        showDetailsViewModel.getShowLiveData().observe(viewLifecycleOwner, { show ->
            updateShow(show)
        })

        showDetailsViewModel.getReviewLiveData().observe(viewLifecycleOwner, { review ->
            updateReviews(review)
        })

        showDetailsViewModel.getReviewsLiveData().observe(viewLifecycleOwner, { reviews ->
            updateReviews(reviews)
        })

        initWriteReviewButton()
        initReviewsRecycler()
    }

    private fun updateShow(show: Show) {
        binding.toolbarLayout.title = show.title
        if (binding.showImage.drawable == null) {
            Glide.with(this).load(show.imageUrl).into(binding.showImage)
        }
        binding.showDescription.text = show.description
        updateRatingInfo(show.numberOfReviews, show.averageRating)
    }

    private fun updateRatingInfo(numberOfReviews: Int, averageRating: Float?) {
        if (numberOfReviews == 0) {
            binding.reviewsRecyclerView.visibility = View.GONE
            binding.showReviewRating.visibility = View.GONE
            binding.showRatingBar.visibility = View.GONE
            binding.noReviewsYet.visibility = View.VISIBLE
        } else if (averageRating != null) {
            binding.noReviewsYet.visibility = View.GONE
            binding.reviewsRecyclerView.visibility = View.VISIBLE
            binding.showReviewRating.visibility = View.VISIBLE
            binding.showRatingBar.visibility = View.VISIBLE
            binding.showReviewRating.text = getString(R.string.reviews_rating_info).format(
                numberOfReviews,
                averageRating
            )
            binding.showRatingBar.rating = averageRating
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

            showDetailsViewModel.addReview(
                bottomSheetBinding.starRatingBar.rating.toInt(),
                bottomSheetBinding.commentInput.text.toString(),
                args.showID.toInt()
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

