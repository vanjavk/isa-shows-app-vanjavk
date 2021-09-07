package me.vanjavk.isa_shows_app_vanjavk.fragments

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.vanjavk.isa_shows_app_vanjavk.*
import me.vanjavk.isa_shows_app_vanjavk.databinding.FragmentLoginBinding
import me.vanjavk.isa_shows_app_vanjavk.networking.Status
import me.vanjavk.isa_shows_app_vanjavk.repository.LoginRepository
import me.vanjavk.isa_shows_app_vanjavk.repository.ShowsRepository
import me.vanjavk.isa_shows_app_vanjavk.viewmodels.LoginViewModel
import me.vanjavk.isa_shows_app_vanjavk.viewmodels.ViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val args: LoginFragmentArgs by navArgs()

    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory(
            requireActivity().getPreferences(Context.MODE_PRIVATE),
            LoginRepository(requireActivity())
        )
    }

    private var emailValid = false
    private var passwordValid = false
    private var loginInProcess = false
    private var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)

        val loggedIn = sharedPref.getBoolean(REMEMBER_ME_KEY, false)
        if (loggedIn) {
            LoginFragmentDirections.actionLoginToShows()
                .let { findNavController().navigate(it) }
        }

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = args.email
        if (email != null) {
            initFromRegister(email)
        }

        loginViewModel.getLoginResultLiveData()
            .observe(this.viewLifecycleOwner) { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        loginInProcess = true
                        snackbar = Snackbar.make(
                            view,
                            getString(R.string.logging_in),
                            Snackbar.LENGTH_SHORT
                        )
                        snackbar?.show()
                    }
                    Status.SUCCESS -> {
                        loginInProcess = false
                        snackbar?.dismiss()
                        LoginFragmentDirections.actionLoginToShows()
                            .let { findNavController().navigate(it) }
                    }
                    Status.ERROR -> {
                        loginInProcess = false
                        if (resource.data == true) {
                            binding.passwordInputLayout.error =
                                getString(R.string.error_email_and_password_combination)
                            snackbar?.dismiss()
                        } else {
                            snackbar = Snackbar.make(
                                view,
                                getString(R.string.error_no_internet),
                                Snackbar.LENGTH_LONG
                            )
                            snackbar?.show()
                        }

                    }
                }
                checkLoginButtonEnableable()
            }
        initLoginButton()
        initRegisterButton()
    }

    private fun initRegisterButton() {
        binding.registerButton.setOnClickListener {
//            LoginFragmentDirections.actionLoginToRegister()
//                .let { findNavController().navigate(it) }

            CoroutineScope(Dispatchers.IO).launch {
                saveBitmap(requireContext(),
                    Glide.with(requireActivity())
                    .asBitmap()
                    .load("https://i.imgur.com/4HFRb2z.jpg") // sample image
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal) // need placeholder to avoid issue like glide annotations
                    .error(android.R.drawable.stat_notify_error) // need error to avoid issue like glide annotations
                    .submit()
                    .get(),Bitmap.CompressFormat.JPEG,"image/jpeg","naruto.jpg")
            }
        }
    }

    private fun saveImage(image: Bitmap): String? {
        var savedImagePath: String? = null
        val imageFileName = "JPEG_" + "FILE_NAME" + ".jpg"
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString()// + "/YOUR_FOLDER_NAME"
        )
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.getAbsolutePath()
            try {
                val fOut: OutputStream = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath)
            //Toast.makeText(this, "IMAGE SAVED", Toast.LENGTH_LONG).show() // to make this working, need to manage coroutine, as this execution is something off the main thread
        }
        return savedImagePath
    }
    private fun galleryAddPic(imagePath: String?) {
        imagePath?.let { path ->
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(path)
            val contentUri: Uri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            requireActivity().sendBroadcast(mediaScanIntent)
        }
    }


    val relativeLocation = Environment.DIRECTORY_DCIM + File.separator + "YourSubforderName"
    fun saveBitmap(
        context: Context, bitmap: Bitmap, format: Bitmap.CompressFormat,
        mimeType: String, displayName: String
    ): Uri {

        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
        }

        var uri: Uri? = null

        return runCatching {
            with(context.contentResolver) {
                insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)?.also {
                    uri = it // Keep uri reference so it can be removed on failure

                    openOutputStream(it)?.use { stream ->
                        if (!bitmap.compress(format, 95, stream))
                            throw IOException("Failed to save bitmap.")
                    } ?: throw IOException("Failed to open output stream.")

                } ?: throw IOException("Failed to create new MediaStore record.")
            }
        }.getOrElse {
            uri?.let { orphanUri ->
                // Don't leave an orphan entry in the MediaStore
                context.contentResolver.delete(orphanUri, null, null)
            }

            throw it
        }
    }


    private fun initFromRegister(email: String) {
        binding.loginText.text = getString(R.string.registration_successful)
        binding.registerButton.visibility = GONE
        binding.emailInput.setText(email)
        checkEmailValid()
    }

    private fun initLoginButton() {
        binding.emailInput.doAfterTextChanged {
            checkEmailValid()
            checkLoginButtonEnableable()
        }
        binding.passwordInput.doAfterTextChanged {
            checkPasswordValid()
            checkLoginButtonEnableable()
        }

        binding.loginButton.setOnClickListener {
            loginInProcess = true
            checkLoginButtonEnableable()
            loginViewModel.login(
                binding.emailInput.text.toString(),
                binding.passwordInput.text.toString(),
                binding.rememberMeCheckBox.isChecked
            )
        }

    }


    private fun checkEmailValid() {
        if (binding.emailInput.text.toString().isValidEmail()) {
            binding.emailInputLayout.error = getString(R.string.invalid_email)
            emailValid = false
        } else {
            binding.emailInputLayout.error = null
            emailValid = true
        }
    }

    private fun checkPasswordValid() {
        if (binding.passwordInput.text.toString().length < MIN_PASSWORD_LENGTH) {
            binding.passwordInputLayout.error = getString(R.string.password_too_short)
            passwordValid = false
        } else {
            binding.passwordInputLayout.error = null
            passwordValid = true
        }
    }

    private fun checkLoginButtonEnableable() {
        binding.loginButton.isEnabled = passwordValid && emailValid && !loginInProcess
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}