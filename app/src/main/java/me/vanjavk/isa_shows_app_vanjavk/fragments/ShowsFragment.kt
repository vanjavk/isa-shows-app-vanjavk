package me.vanjavk.isa_shows_app_vanjavk.fragments

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import me.vanjavk.isa_shows_app_vanjavk.adapters.ShowsAdapter
import me.vanjavk.isa_shows_app_vanjavk.databinding.DialogUserProfileBinding
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentShowsBinding
import me.vanjavk.isa_shows_app_vanjavk.utils.FileUtil.createImageFile
import me.vanjavk.isa_shows_app_vanjavk.utils.FileUtil.getImageFile
import me.vanjavk.isa_shows_app_vanjavk.viewmodels.ShowsViewModel
import me.vanjavk.isa_shows_app_vanjavk.viewmodels.ViewModelFactory
import java.io.File
import java.util.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import me.vanjavk.isa_shows_app_vanjavk.NO_INTERNET_ERROR
import me.vanjavk.isa_shows_app_vanjavk.R
import me.vanjavk.isa_shows_app_vanjavk.getFileFromUri
import me.vanjavk.isa_shows_app_vanjavk.models.Show
import me.vanjavk.isa_shows_app_vanjavk.networking.Status
import me.vanjavk.isa_shows_app_vanjavk.preparePermissionsContract
import me.vanjavk.isa_shows_app_vanjavk.repository.ShowsRepository
import me.vanjavk.isa_shows_app_vanjavk.utils.GlideUrlCustomCacheKey


class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null

    private var recyclerViewViewType = ShowsAdapter.ViewType.VIEW_TYPE_CARD

    private val binding get() = _binding!!

    private lateinit var bottomSheetBinding: DialogUserProfileBinding
    private lateinit var dialog: BottomSheetDialog
    private var snackbar: Snackbar? = null

    private var showsAdapter: ShowsAdapter? = null
    private var topRatedShowsAdapter: ShowsAdapter? = null

    private val showsViewModel: ShowsViewModel by viewModels {
        ViewModelFactory(
            requireActivity().getPreferences(Context.MODE_PRIVATE),
            ShowsRepository(requireActivity())
        )
    }

    private val permissionForCamera = preparePermissionsContract(onPermissionsGranted = {
        val uri = createImageFile(requireContext())?.let {
            FileProvider.getUriForFile(
                requireContext(), requireContext().packageName.toString() + ".fileprovider",
                it
            )
        }
        if (uri == null) {
            Toast.makeText(
                activity,
                getString(R.string.error_opening_file),
                Toast.LENGTH_SHORT
            )
                .show()
            return@preparePermissionsContract
        }

        getCameraImage.launch(uri)
    })

    private val getCameraImage =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                val imageFile = getImageFile(
                    requireContext()
                )
                if (imageFile != null) {
                    showsViewModel.uploadProfilePicture(imageFile)
                }
            }
        }

    private val permissionForFiles = preparePermissionsContract(onPermissionsGranted = {
        selectImageFromGallery.launch("image/*")
    })

    private val selectImageFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri.let {
                if (uri == null) {
                    return@let
                }
                //Prvo trebam kopirati odabranu sliku u aplikacijski folder kako bi se ona mogla pomocu fileutilsa smanjiti, no kako bi dobio File za copy moram koristiti ekstenzijsku funkciju Uri.getFileFromUri() kako bih dobio ispravan file koji mogu kopirati.
                if (uri.getFileFromUri(requireContext())?.copyTo(
                        File(
                            requireContext().getExternalFilesDir(
                                Environment.DIRECTORY_PICTURES
                            ), "avatar.jpg"
                        ), true
                    ) != null
                ) {
                    getImageFile(requireContext())?.let { imageFile ->
                        showsViewModel.uploadProfilePicture(
                            imageFile
                        )
                    }
                } else {
                    Toast.makeText(activity, "Cannot get image from gallery.", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        bottomSheetBinding = DialogUserProfileBinding.inflate(layoutInflater)

        dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(bottomSheetBinding.root)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initShowsRecycler()

        showsViewModel.getShowsLiveData().observe(viewLifecycleOwner, { shows ->
            binding.showsRecyclerView.isVisible = !shows.isNullOrEmpty()
            updateShows(shows)
        })

        showsViewModel.showsResultLiveData.observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    snackbar = Snackbar.make(
                        view,
                        getString(R.string.info_loading_shows),
                        Snackbar.LENGTH_LONG
                    )
                    snackbar?.show()
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                Status.SUCCESS -> {
                    snackbar?.dismiss()
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                Status.ERROR -> {
                    if (resource.data == true) {
                        snackbar?.dismiss()
                    } else {
                        snackbar = Snackbar.make(
                            view,
                            getString(R.string.error_no_internet),
                            Snackbar.LENGTH_LONG
                        )
                        snackbar?.show()
                    }
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
            checkShowsEmpty()
        })

        showsViewModel.topRatedShowsLiveData.observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    snackbar = Snackbar.make(
                        view,
                        getString(R.string.info_loading_shows),
                        Snackbar.LENGTH_LONG
                    )
                    snackbar?.show()
                    resource.data?.let { updateTopRatedShows(it) }
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                Status.SUCCESS -> {
                    snackbar?.dismiss()
                    resource.data?.let { updateTopRatedShows(it) }
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                Status.ERROR -> {
                    if (resource.message != NO_INTERNET_ERROR) {
                        snackbar?.dismiss()
                    } else {
                        snackbar = Snackbar.make(
                            view,
                            getString(R.string.error_no_internet),
                            Snackbar.LENGTH_LONG
                        )
                        snackbar?.show()
                    }
                    resource.data?.let { updateTopRatedShows(it) }
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
            checkShowsEmpty()
        })

        showsViewModel.userLiveData.observe(viewLifecycleOwner, { resource ->
            when (resource.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    updateProfileIcons(resource.data?.imageUrl)
                    bottomSheetBinding.userEmail.text = resource.data?.email
                }
                Status.ERROR -> {
                }
            }
        })

        showsViewModel.changeProfilePictureResultLiveData
            .observe(viewLifecycleOwner, { resource ->
                val dialogView = dialog.window?.decorView ?: return@observe
                when (resource.status) {
                    Status.LOADING -> {
                        snackbar = Snackbar.make(
                            dialogView,
                            getString(R.string.info_uploading_profile_picture),
                            Snackbar.LENGTH_LONG
                        )
                        snackbar?.show()
                    }
                    Status.SUCCESS -> {
                        snackbar = Snackbar.make(
                            dialogView,
                            getString(R.string.info_profile_picture_upload_success),
                            Snackbar.LENGTH_LONG
                        )
                        snackbar?.show()
                    }
                    Status.ERROR -> {
                        if (resource.message != NO_INTERNET_ERROR) {
                            snackbar = Snackbar.make(
                                dialogView,
                                getString(R.string.error_change_profile_picture),
                                Snackbar.LENGTH_LONG
                            )
                            snackbar?.show()
                        } else {
                            snackbar = Snackbar.make(
                                dialogView,
                                getString(R.string.error_no_internet),
                                Snackbar.LENGTH_LONG
                            )
                            snackbar?.show()
                        }
                    }
                }
            })

        showsViewModel.getCurrentUser()
        showsViewModel.fetchShows()
        showsViewModel.fetchTopRatedShows()

        initUserProfileButton()
        initUserProfileBottomSheet()
    }

    private fun updateProfileIcons(imageUrl: String?) {
        setImageFromFile(binding.profileIconImage, imageUrl)
        setImageFromFile(bottomSheetBinding.userProfileImage, imageUrl)
    }

    private fun setImageFromFile(imageView: ImageView, imageUrl: String?) {
        if (imageUrl.isNullOrBlank()) {
            binding.profileIconImage.setImageResource(R.drawable.ic_painting_art)
        } else {
            Glide.with(this).load(GlideUrlCustomCacheKey(imageUrl)).into(imageView)
        }
    }

    private fun updateShows(shows: List<Show>) {
        showsAdapter?.setItems(shows)
    }

    private fun updateTopRatedShows(shows: List<Show>) {
        topRatedShowsAdapter?.setItems(shows)
    }

    private fun checkShowsEmpty() {
        binding.showsRecyclerView.isVisible = binding.showsRecyclerView.adapter?.itemCount != 0
    }

    private fun initUserProfileButton() {
        binding.profileIconImage.setOnClickListener {
            dialog.show()
        }
    }

    private fun initUserProfileBottomSheet() {
        bottomSheetBinding.changeProfilePictureButton.setOnClickListener {
            handleChangeProfilePicture()
        }

        bottomSheetBinding.logoutButton.setOnClickListener {
            logout()
            dialog.dismiss()
        }
    }

    private fun logout() {
        showsViewModel.logout()
        ShowsFragmentDirections.actionLogout()
            .let { findNavController().navigate(it) }
    }

    private fun handleChangeProfilePicture() {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(getString(R.string.choose_picture_source))
                setItems(
                    arrayOf(
                        getString(R.string.camera_take_a_photo),
                        getString(R.string.gallery_choose_a_photo)
                    )
                ) { _, which ->
                    when (which) {
                        0 -> permissionForCamera.launch(arrayOf(Manifest.permission.CAMERA))
                        1 -> permissionForFiles.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                    }
                }
                setNegativeButton(
                    getString(R.string.cancel)
                ) { _, _ ->
                }
            }
            builder.create()
        }
        alertDialog?.show()
    }

    private fun initShowsRecycler() {

        initTopRatedShowsChip()

        showsAdapter = ShowsAdapter(emptyList()) { item ->
            ShowsFragmentDirections.actionShowToDetails(
                item.id
            )
                .let { findNavController().navigate(it) }
        }.apply {
            stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        topRatedShowsAdapter = ShowsAdapter(emptyList()) { item ->
            ShowsFragmentDirections.actionShowToDetails(
                item.id
            )
                .let { findNavController().navigate(it) }
        }.apply {
            stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        binding.showsRecyclerView.layoutManager = object : LinearLayoutManager(activity) {
            override fun getExtraLayoutSpace(state: RecyclerView.State): Int {
                return requireContext().resources.getDimensionPixelSize(R.dimen.recycler_view_extra_space)
            }
        }
        binding.showsRecyclerView.adapter = showsAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshShows(binding.topRatedShowChip.isChecked)
        }
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.purple_infinum_dark)

        binding.changeLayoutFAB.setOnClickListener {
            when (recyclerViewViewType) {
                ShowsAdapter.ViewType.VIEW_TYPE_CARD -> {
                    setViewType(ShowsAdapter.ViewType.VIEW_TYPE_GRID)
                }
                ShowsAdapter.ViewType.VIEW_TYPE_GRID -> {
                    setViewType(ShowsAdapter.ViewType.VIEW_TYPE_CARD)
                }
            }
        }
    }

    private fun initTopRatedShowsChip() {
        binding.topRatedShowChip.setOnCheckedChangeListener { _, isChecked ->
            binding.showsRecyclerView.adapter =
                if (isChecked) topRatedShowsAdapter else showsAdapter
            refreshShows(isChecked)
            checkShowsEmpty()
            setViewType()
        }
    }

    private fun setViewType(viewType: ShowsAdapter.ViewType = recyclerViewViewType) {
        recyclerViewViewType = viewType
        //val recyclerBinding = (binding.showsRecyclerView.adapter as ShowsAdapter)
        when (recyclerViewViewType) {
            ShowsAdapter.ViewType.VIEW_TYPE_CARD -> {
                binding.changeLayoutFAB.setImageResource(R.drawable.ic_fab_grid)
                binding.showsRecyclerView.layoutManager = object : LinearLayoutManager(activity) {
                    override fun getExtraLayoutSpace(state: RecyclerView.State): Int {
                        return requireContext().resources.getDimensionPixelSize(R.dimen.recycler_view_extra_space)
                    }
                }
            }
            ShowsAdapter.ViewType.VIEW_TYPE_GRID -> {
                binding.changeLayoutFAB.setImageResource(R.drawable.ic_fab_card)
                binding.showsRecyclerView.layoutManager = object : GridLayoutManager(activity, 2) {
                    override fun getExtraLayoutSpace(state: RecyclerView.State): Int {
                        return requireContext().resources.getDimensionPixelSize(R.dimen.recycler_view_extra_space)
                    }
                }
            }

        }
        (binding.showsRecyclerView.adapter as ShowsAdapter).setViewType(recyclerViewViewType)
        binding.showsRecyclerView.adapter =
            if (binding.topRatedShowChip.isChecked) topRatedShowsAdapter else showsAdapter
    }

    private fun refreshShows(isTopRatedShowsChecked: Boolean) {
        if (isTopRatedShowsChecked) {
            showsViewModel.fetchTopRatedShows()
        } else {
            showsViewModel.fetchShows()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}