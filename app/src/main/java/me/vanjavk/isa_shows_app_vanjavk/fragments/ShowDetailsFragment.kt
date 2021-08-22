package me.vanjavk.isa_shows_app_vanjavk.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import me.vanjavk.isa_shows_app_vanjavk.R
import me.vanjavk.isa_shows_app_vanjavk.adapters.ReviewsAdapter
import me.vanjavk.isa_shows_app_vanjavk.databinding.DialogAddReviewBinding
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentShowDetailsBinding
import me.vanjavk.isa_shows_app_vanjavk.models.Review
import me.vanjavk.isa_shows_app_vanjavk.models.Show
import me.vanjavk.isa_shows_app_vanjavk.repository.ShowDetailsRepository
import me.vanjavk.isa_shows_app_vanjavk.utils.GlideUrlCustomCacheKey
import me.vanjavk.isa_shows_app_vanjavk.viewmodels.ShowDetailsViewModel
import me.vanjavk.isa_shows_app_vanjavk.viewmodels.ViewModelFactory


class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null

    private val binding get() = _binding!!

    private lateinit var bottomSheetBinding: DialogAddReviewBinding
    private lateinit var dialog: BottomSheetDialog
    private var snackbar: Snackbar? = null

    private val args: ShowDetailsFragmentArgs by navArgs()

    private var reviewsAdapter: ReviewsAdapter? = null

    private val showDetailsViewModel: ShowDetailsViewModel by viewModels {
        ViewModelFactory(
            requireActivity().getPreferences(Context.MODE_PRIVATE),
            ShowDetailsRepository(requireActivity())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true);

        bottomSheetBinding = DialogAddReviewBinding.inflate(layoutInflater)

        dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(bottomSheetBinding.root)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val showId = args.showID

        showDetailsViewModel.fetchShow(showId)
        showDetailsViewModel.fetchReviews(showId)

        showDetailsViewModel.getShowLiveData(showId).observe(viewLifecycleOwner, { show ->
            if (show.isNotEmpty()) {
                updateShow(show.first())
            }
        })

        showDetailsViewModel.getReviewsLiveData(showId).observe(
            viewLifecycleOwner,
            { reviews ->
                if (binding.swipeRefreshLayout.isRefreshing){
                    updateReviews(reviews)
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            })

        initSupportActionBar()
        initWriteReviewButton()
        initReviewsRecycler(showId)
        initAddReviewBottomSheet()
    }

    private fun initSupportActionBar() {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbarLayout.title = getString(R.string.loading)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun updateShow(show: Show) {
        binding.toolbarLayout.title = show.title
        Glide.with(this).load(GlideUrlCustomCacheKey(show.imageUrl)).into(binding.showImage)
        binding.showDescription.text = show.description
        updateRatingInfo(show.numberOfReviews, show.averageRating)
    }

    private fun updateRatingInfo(numberOfReviews: Int, averageRating: Float?) {
        val noReviews = numberOfReviews == 0
        binding.reviewsRecyclerView.isVisible = !noReviews
        binding.showReviewRating.isVisible = !noReviews
        binding.showRatingBar.isVisible = !noReviews
        binding.noReviewsYet.isVisible = noReviews
        if (!noReviews) {
            binding.showReviewRating.text = getString(R.string.reviews_rating_info).format(
                numberOfReviews,
                averageRating
            )
            binding.showRatingBar.rating = averageRating ?: 0f
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
            bottomSheetBinding.starRatingBar.rating = 0f
            bottomSheetBinding.commentInput.text?.clear()
            dialog.show()
        }
    }

    private fun initAddReviewBottomSheet() {
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

        bottomSheetBinding.closeButton.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun initReviewsRecycler(showId: String) {
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.purple_infinum_dark)
        binding.swipeRefreshLayout.isRefreshing = true

        reviewsAdapter = ReviewsAdapter(emptyList())

        binding.reviewsRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.reviewsRecyclerView.adapter = reviewsAdapter
        binding.reviewsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )


        binding.swipeRefreshLayout.setOnRefreshListener {
            showDetailsViewModel.fetchReviews(showId = showId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

