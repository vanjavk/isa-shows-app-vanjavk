package me.vanjavk.isa_shows_app_vanjavk

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import me.vanjavk.isa_shows_app_vanjavk.FileUtil.createImageFile
import me.vanjavk.isa_shows_app_vanjavk.FileUtil.getImageFile
import me.vanjavk.isa_shows_app_vanjavk.databinding.DialogUserProfileBinding
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentShowsBinding
import me.vanjavk.isa_shows_app_vanjavk.model.Show
import me.vanjavk.isa_shows_app_vanjavk.viewmodel.ShowsViewModel
import java.util.*

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null

    private val binding get() = _binding!!

    private var showsAdapter: ShowsAdapter? = null

    private val showsViewModel: ShowsViewModel by navGraphViewModels(R.id.main)

    private val permissionForCamera = preparePermissionsContract(onPermissionsGranted = {
        // do camera
        camera()
    })
    private lateinit var uri : Uri
    private fun camera() {
        uri = createImageFile(requireContext())?.let {
            FileProvider.getUriForFile(requireContext(), "me.vanjavk.isa-shows-app-vanjavk.fileprovider",
                it
            )
        }!!

        getCameraImage.launch(uri)

    }
    private val getCameraImage = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            Log.i(TAG, "Got image at: $uri")
            val imageFile = getImageFile(
                requireContext()
            )
            if (imageFile != null) {
                Log.i(TAG, "Got image at: $uri")
                binding.profileIconImage.setImageURI(Uri.fromFile(imageFile))
            }

        }
    }

    private fun setImageFromFile() {

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

        initUserProfileButton()

        setImageFromStorage(binding.profileIconImage)
    }

    fun setImageFromStorage(imageView: ImageView){
        val imageFile = getImageFile(
            requireContext()
        )
        if (imageFile != null) {
            imageView.setImageURI(Uri.fromFile(imageFile))
        }else{
            imageView.setImageResource(R.drawable.ic_painting_art)
        }
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

        val dialog = BottomSheetDialog(activity)

        val bottomSheetBinding = DialogUserProfileBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        setImageFromStorage(bottomSheetBinding.userProfileImage)


        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        if (sharedPref == null) {
            Toast.makeText(activity, "Action failed. Aborting...", Toast.LENGTH_SHORT).show()
            activity.onBackPressed()
            return
        }
        val email =
            sharedPref.getString(getString(R.string.user_email_key), "Default_user").orEmpty()
        bottomSheetBinding.userEmail.text=email

        bottomSheetBinding.changeProfilePhotoButton.setOnClickListener {
            permissionForCamera.launch(arrayOf(Manifest.permission.CAMERA))
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