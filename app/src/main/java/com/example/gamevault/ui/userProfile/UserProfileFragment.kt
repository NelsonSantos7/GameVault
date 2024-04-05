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

        // Carregar a imagem do perfil ao abrir o fragmento
        loadImageProfile()

        val sharedPreferences = activity?.getSharedPreferences("com.example.gamevault.prefs", Context.MODE_PRIVATE)
        val userName = sharedPreferences?.getString("USERNAME", "Nome não disponível")
        val userEmail = sharedPreferences?.getString("USER_EMAIL", "Email não disponível")
        binding.userProfileName.text = userName
        binding.userProfileEmail.text = userEmail

        binding.userProfileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data
            selectedImageUri?.let { uri ->
                startCrop(uri)
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            binding.userProfileImage.setImageURI(resultUri)

            // Salvar a URI recortada no perfil do usuário
            saveImageProfileUri(resultUri.toString())
        }
    }

    private fun saveImageProfileUri(imageUri: String) {
        val sharedPreferences = activity?.getSharedPreferences("com.example.gamevault.prefs", Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.putString("USER_PROFILE_IMAGE_URI", imageUri)?.apply()
    }

    private fun loadImageProfile() {
        val sharedPreferences = activity?.getSharedPreferences("com.example.gamevault.prefs", Context.MODE_PRIVATE)
        val imageUri = sharedPreferences?.getString("USER_PROFILE_IMAGE_URI", null)
        if (imageUri != null) {
            binding.userProfileImage.setImageURI(Uri.parse(imageUri))
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
        private const val REQUEST_CROP = UCrop.REQUEST_CROP
    }
}

