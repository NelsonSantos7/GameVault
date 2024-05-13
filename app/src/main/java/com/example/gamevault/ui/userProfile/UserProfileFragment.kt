package com.example.gamevault.ui.userProfile

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gamevault.databinding.FragmentUserProfileBinding
import com.yalantis.ucrop.UCrop
import java.io.File

class UserProfileFragment : Fragment() {
    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadImageProfile()
        setupUserProfile()
        setupImagePickerListener()
    }

    private fun setupUserProfile() {
        val sharedPreferences = activity?.getSharedPreferences("com.example.gamevault.prefs", Context.MODE_PRIVATE)
        val userName = sharedPreferences?.getString("USERNAME", "Nome não disponível")
        val userEmail = sharedPreferences?.getString("USER_EMAIL", "Email não disponível")
        binding.userProfileName.text = userName
        binding.userProfileEmail.text = userEmail
    }

    private fun setupImagePickerListener() {
        binding.userProfileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                startCrop(uri)
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            UCrop.getOutput(data!!)?.let { resultUri ->
                binding.userProfileImage.setImageURI(resultUri)
                saveImageProfileUri(resultUri.toString())
            }
        }
    }

    private fun saveImageProfileUri(imageUri: String) {
        activity?.getSharedPreferences("com.example.gamevault.prefs", Context.MODE_PRIVATE)?.edit()?.putString("USER_PROFILE_IMAGE_URI", imageUri)?.apply()
    }

    private fun loadImageProfile() {
        activity?.getSharedPreferences("com.example.gamevault.prefs", Context.MODE_PRIVATE)?.getString("USER_PROFILE_IMAGE_URI", null)?.let {
            binding.userProfileImage.setImageURI(Uri.parse(it))
        }
    }

    private fun startCrop(uri: Uri) {
        UCrop.of(uri, Uri.fromFile(File(requireContext().cacheDir, "IMG_${System.currentTimeMillis()}.jpg")))
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(500, 500)
            .start(requireContext(), this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1000
    }
}
