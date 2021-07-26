package me.vanjavk.isa_shows_app_vanjavk

import Show
import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialog
import me.vanjavk.isa_shows_app_vanjavk.FileUtil.createImageFile
import me.vanjavk.isa_shows_app_vanjavk.FileUtil.getImageFile
import me.vanjavk.isa_shows_app_vanjavk.adapter.ShowsAdapter
import me.vanjavk.isa_shows_app_vanjavk.databinding.DialogUserProfileBinding
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentShowsBinding
import me.vanjavk.isa_shows_app_vanjavk.viewmodel.ShowsViewModel
import me.vanjavk.isa_shows_app_vanjavk.viewmodel.ShowsViewModelFactory
import java.util.*

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null

    private val binding get() = _binding!!

    private var showsAdapter: ShowsAdapter? = null

    private lateinit var showsViewModel: ShowsViewModel
    private lateinit var showsViewModelFactory: ShowsViewModelFactory

    private val permissionForFiles = preparePermissionsContract(onPermissionsGranted = {
        val uri = createImageFile(requireContext())?.let {
            FileProvider.getUriForFile(
                requireContext(), "me.vanjavk.isa-shows-app-vanjavk.fileprovider",
                it
            )
        }
        if (uri == null) {
            Toast.makeText(activity, getString(R.string.error_opening_file), Toast.LENGTH_SHORT)
                .show()
            return@preparePermissionsContract
        }

        openFile(uri)
    })

    private fun openFile(pickerInitialUri: Uri) {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*";

            putExtra(MediaStore.EXTRA_OUTPUT, pickerInitialUri)
        }
        startActivityForResult(intent, 2)
    }

    private val permissionForCamera = preparePermissionsContract(onPermissionsGranted = {
        val uri = createImageFile(requireContext())?.let {
            FileProvider.getUriForFile(
                requireContext(), requireContext().packageName.toString() +".fileprovider",
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
                    //setImageFromFile()
                    //showsViewModel.setImage(imageFile)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)

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

        showsViewModelFactory = ShowsViewModelFactory(sharedPref)
        showsViewModel = ViewModelProvider(this, showsViewModelFactory)
            .get(ShowsViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initShowsRecycler()

        showsViewModel.getShowsLiveData().observe(viewLifecycleOwner, { shows ->
            updateItems(shows)
        })

        showsViewModel.getUserLiveData().observe(viewLifecycleOwner, { user ->
            setImageFromFile(binding.profileIconImage, user.imageUrl)
        })

        showsViewModel.getShowsResultLiveData()
            .observe(viewLifecycleOwner, { isGetShowsSuccessful ->
                if (!isGetShowsSuccessful) {
                    logout()
//                    Toast.makeText(activity, "NEUSPJESNO", Toast.LENGTH_SHORT).show()
                }
            })

        showsViewModel.getShows()

//        val imageFile = getImageFile(
//            requireContext()
//        )
//        if (imageFile != null) {
//            showsViewModel.setImage(imageFile)
//        }

        initUserProfileButton()
    }

    private fun setImageFromFile(imageView: ImageView, imageFile: String?) {
        if(imageFile!=null){
            Glide.with(this).load(imageFile).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(imageView)
        }else{
            binding.profileIconImage.setImageResource(R.drawable.ic_painting_art)
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

        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        if (sharedPref == null) {
            Toast.makeText(
                activity,
                getString(R.string.error_shared_pref_is_null),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val dialog = BottomSheetDialog(activity)

        val bottomSheetBinding = DialogUserProfileBinding.inflate(layoutInflater)

        dialog.setContentView(bottomSheetBinding.root)
        val email =
            sharedPref.getString(getString(R.string.user_email_key), "Default_user").orEmpty()



        showsViewModel.getUserLiveData().observe(viewLifecycleOwner, { user ->
            setImageFromFile(bottomSheetBinding.userProfileImage, user.imageUrl)
        })
//        showsViewModel.getUserProfilePictureLiveData().observe(viewLifecycleOwner, { imageFile ->
//            setImageFromFile(bottomSheetBinding.userProfileImage, imageFile)
//        })

//        val imageFile = getImageFile(
//            requireContext()
//        )
//        if (imageFile != null) {
//            setImageFromFile(bottomSheetBinding.userProfileImage, imageFile)
//        }

        bottomSheetBinding.userEmail.text = email

        bottomSheetBinding.changeProfilePictureButton.setOnClickListener {
            handleChangeProfilePicture()
        }

        bottomSheetBinding.logoutButton.setOnClickListener {
            logout()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun logout() {
        with(showsViewModel.getSharedPreferences().edit()) {
            putBoolean(
                getString(me.vanjavk.isa_shows_app_vanjavk.R.string.logged_in_key),
                false
            )
            apply()
        }
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
                        1 -> Toast.makeText(
                            activity,
                            "Ovo ne radi, s obzirom da je pod extra, predao sam zadacu bez toga.",
                            Toast.LENGTH_SHORT
                        )
                            .show()//permissionForFiles.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
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