package me.vanjavk.isa_shows_app_vanjavk

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialog
import me.vanjavk.isa_shows_app_vanjavk.FileUtil.createImageFile
import me.vanjavk.isa_shows_app_vanjavk.FileUtil.getImageFile
import me.vanjavk.isa_shows_app_vanjavk.databinding.DialogUserProfileBinding
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentShowsBinding
import me.vanjavk.isa_shows_app_vanjavk.model.Show
import me.vanjavk.isa_shows_app_vanjavk.viewmodel.ShowsViewModel
import java.io.File
import java.util.*

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null

    private val binding get() = _binding!!

    private var showsAdapter: ShowsAdapter? = null

    private val showsViewModel: ShowsViewModel by viewModels()

    private val permissionForCamera = preparePermissionsContract(onPermissionsGranted = {
        val uri = createImageFile(requireContext())?.let {
            FileProvider.getUriForFile(
                requireContext(), "me.vanjavk.isa-shows-app-vanjavk.fileprovider",
                it
            )
        }
        if (uri == null) {
            Toast.makeText(activity, "Error! Couldn't open file from storage.", Toast.LENGTH_SHORT)
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
                    showsViewModel.setImage(imageFile)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showsViewModel.initShows()

        initShowsRecycler()

        showsViewModel.getShowsLiveData().observe(viewLifecycleOwner, { shows ->
            updateItems(shows)
        })

        showsViewModel.getUserProfilePictureLiveData().observe(viewLifecycleOwner, { imageFile ->
            setImageFromFile(binding.profileIconImage, imageFile)
        })

        val imageFile = getImageFile(
            requireContext()
        )
        if (imageFile != null) {
            showsViewModel.setImage(imageFile)
        }
        initUserProfileButton()
    }

    private fun setImageFromFile(imageView: ImageView, imageFile: File) {
        Glide.with(this).load(imageFile).diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true).into(imageView)

//        if (imageFile != null) {
//            imageView.setImageURI(Uri.fromFile(imageFile))
//        }else{
//            imageView.setImageResource(R.drawable.ic_painting_art)
//        }
    }

    private fun updateItems(shows: List<Show>) {
        showsAdapter?.setItems(shows)
    }

    private fun initUserProfileButton() {
        binding.profileIconImage.setOnClickListener {
            userProfileBottomSheet()
        }
    }

    private fun userProfileBottomSheet() {

        val activity = activity as AppCompatActivity

        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        if (sharedPref == null) {
            Toast.makeText(activity, "Action failed. Aborting...", Toast.LENGTH_SHORT).show()
            return
        }

        val dialog = BottomSheetDialog(activity)

        val bottomSheetBinding = DialogUserProfileBinding.inflate(layoutInflater)

        dialog.setContentView(bottomSheetBinding.root)
        val email =
            sharedPref.getString(getString(R.string.user_email_key), "Default_user").orEmpty()

        showsViewModel.getUserProfilePictureLiveData().observe(viewLifecycleOwner, { imageFile ->
            setImageFromFile(bottomSheetBinding.userProfileImage, imageFile)
        })

        val imageFile = getImageFile(
            requireContext()
        )
        if (imageFile != null) {
            setImageFromFile(bottomSheetBinding.userProfileImage, imageFile)
        }

        bottomSheetBinding.userEmail.text = email

        bottomSheetBinding.changeProfilePictureButton.setOnClickListener {
            handleChangeProfilePicture()
        }

        bottomSheetBinding.logoutButton.setOnClickListener {
            with(sharedPref.edit()) {
                putBoolean(
                    getString(me.vanjavk.isa_shows_app_vanjavk.R.string.logged_in_key),
                    false
                )
                apply()
            }
            ShowsFragmentDirections.actionLogout()
                .let { findNavController().navigate(it) }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun handleChangeProfilePicture() {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Choose image source:")
                setItems(
                    arrayOf("Camera: Take a photo", "Storage: Browse files", "Cancel"),
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which){
                            0 -> permissionForCamera.launch(arrayOf(Manifest.permission.CAMERA))
                        }
                        // The 'which' argument contains the index position
                        // of the selected item
                    })
                setPositiveButton(getString(R.string.continue_string),
                    DialogInterface.OnClickListener { dialog, id ->


                        // User clicked OK button
                    })
                setNegativeButton(getString(R.string.cancel),
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            }
            // Set other dialog properties


            // Create the AlertDialog
            builder.create()
        }
        alertDialog?.show()
    }

    private fun initShowsRecycler() {
        showsAdapter = ShowsAdapter(emptyList()) { item ->
            ShowsFragmentDirections.actionShowToDetails(
                item.id
            )
                .let { findNavController().navigate(it) }
        }

        binding.showsRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.showsRecyclerView.adapter = showsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}