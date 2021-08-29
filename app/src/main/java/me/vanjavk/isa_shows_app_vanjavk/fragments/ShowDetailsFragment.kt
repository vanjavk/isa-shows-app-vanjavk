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
import me.vanjavk.isa_shows_app_vanjavk.*
import me.vanjavk.isa_shows_app_vanjavk.adapters.ReviewsAdapter
import me.vanjavk.isa_shows_app_vanjavk.databinding.DialogAddReviewBinding
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentShowDetailsBinding
import me.vanjavk.isa_shows_app_vanjavk.models.Review
import me.vanjavk.isa_shows_app_vanjavk.models.Show
import me.vanjavk.isa_shows_app_vanjavk.models.User
import me.vanjavk.isa_shows_app_vanjavk.networking.Status
import me.vanjavk.isa_shows_app_vanjavk.repository.ShowDetailsRepository
import me.vanjavk.isa_shows_app_vanjavk.utils.GlideUrlCustomCacheKey
import me.vanjavk.isa_shows_app_vanjavk.viewmodels.ShowDetailsViewModel
import me.vanjavk.isa_shows_app_vanjavk.viewmodels.ViewModelFactory
import java.util.*


class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null

    private val binding get() = _binding!!

    private lateinit var bottomSheetBinding: DialogAddReviewBinding
    private lateinit var dialog: BottomSheetDialog
    private var snackbar: Snackbar? = null

    private val args: ShowDetailsFragmentArgs by navArgs()

    private var reviewsAdapter: ReviewsAdapter? = null
    private var refreshed = false
    private val reviewsQueue: Queue<Review> = LinkedList<Review>()

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
        binding.swipeRefreshLayout.isRefreshing = true
        showDetailsViewModel.reviewsResultLiveData.observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                Status.ERROR -> {
                    binding.swipeRefreshLayout.isRefreshing = false

                    if (resource.message != NO_INTERNET_ERROR) {
                    } else {
                        snackbar = Snackbar.make(
                            view,
                            getString(R.string.error_no_internet),
                            Snackbar.LENGTH_LONG
                        ).apply {
                            anchorView = binding.bottomWriteReviewBar
                        }
                        snackbar?.show()
                    }
                }
            }
        })


        showDetailsViewModel.addReviewLiveData.observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    resource.data?.let {
                        if (refreshed) {
                            updateReviews(it)
                        } else {
                            reviewsQueue.add(it)
                        }
                    }
                }
            }

        })

        showDetailsViewModel.addReviewResultLiveData.observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    snackbar = Snackbar.make(
                        view,
                        getString(R.string.info_adding_review),
                        Snackbar.LENGTH_LONG
                    ).apply {
                        anchorView = binding.bottomWriteReviewBar
                    }
                    snackbar?.show()
                }
                Status.SUCCESS -> {
                    snackbar = Snackbar.make(
                        view,
                        getString(R.string.info_review_successfully_added),
                        Snackbar.LENGTH_LONG
                    ).apply {
                        anchorView = binding.bottomWriteReviewBar
                    }
                    snackbar?.show()
                }
                Status.ERROR -> {

                    if (resource.message != NO_INTERNET_ERROR) {
                        snackbar = Snackbar.make(
                            view,
                            getString(R.string.error_add_review),
                            Snackbar.LENGTH_LONG
                        ).apply {
                            anchorView = binding.bottomWriteReviewBar
                        }
                        snackbar?.show()
                    } else {
                        snackbar = Snackbar.make(
                            view,
                            getString(R.string.error_no_internet),
                            Snackbar.LENGTH_LONG
                        ).apply {
                            anchorView = binding.bottomWriteReviewBar
                        }
                        snackbar?.show()
                    }
                }
            }
        })

        showDetailsViewModel.getShowLiveData(showId).observe(viewLifecycleOwner, { show ->
            if (show.isNotEmpty()) {
                updateShow(show.first())
            }
        })

        showDetailsViewModel.getReviewsLiveData(showId).observe(
            viewLifecycleOwner,
            { reviews ->
                if (!refreshed && !reviews.isNullOrEmpty()) {
                    updateReviews(reviews)
                    binding.swipeRefreshLayout.isRefreshing = false
                    refreshed = true
                }
            })

        initSupportActionBar()
        initWriteReviewButton()
        initReviewsRecycler(showId)
        initAddReviewBottomSheet()
        checkShowReviewEmptyState()
    }

    private fun initSupportActionBar() {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbarLayout.title = getString(R.string.loading)
        binding.toolbar.setNavigationOnClickListener {
            snackbar?.dismiss()
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
        if (!noReviews) {
            binding.showReviewRating.text = getString(R.string.reviews_rating_info).format(
                numberOfReviews,
                averageRating
            )
            binding.showRatingBar.rating = averageRating ?: 0f
        }
        checkShowReviewEmptyState()
    }

    private fun checkShowReviewEmptyState(){
        val noReviews = reviewsAdapter?.itemCount == 0 ?: false
        binding.noReviewsYet.isVisible = noReviews
    }

    private fun updateReviews(reviews: List<Review>) {
        reviewsAdapter?.setItems(reviews)
        while (reviewsQueue.count() > 0) {
            updateReviews(reviewsQueue.remove())
        }
        checkShowReviewEmptyState()
    }

    private fun updateReviews(review: Review) {
        reviewsAdapter?.addItem(review)
        checkShowReviewEmptyState()
    }

    private fun initWriteReviewButton() {
        binding.writeReviewButton.setOnClickListener {
            bottomSheetBinding.starRatingBar.rating = 0f
            bottomSheetBinding.commentInput.text?.clear()
            bottomSheetBinding.confirmButton.isEnabled = false
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
            refreshed = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

