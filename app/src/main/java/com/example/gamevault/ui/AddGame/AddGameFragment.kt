package com.example.gamevault.ui.AddGame

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.gamevault.R
import com.example.gamevault.SQLite.DBhelper
import com.example.gamevault.databinding.FragmentAddGameBinding
import com.example.gamevault.model.Gamemodel
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddGameFragment : Fragment() {
    private var _binding: FragmentAddGameBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DBhelper
    private var imageUri: Uri? = null
    private lateinit var selectedPlatforms: BooleanArray
    private lateinit var platformsList: Array<String>
    private var chosenPlatforms: String = ""
    private var chosenStatus: Int = 0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddGameBinding.inflate(inflater, container, false)
        databaseHelper = DBhelper(requireContext())
        platformsList = resources.getStringArray(R.array.platform_options)
        selectedPlatforms = BooleanArray(platformsList.size)
        sharedPreferences = requireContext().getSharedPreferences("com.example.gamevault.prefs", Context.MODE_PRIVATE)

        setListeners()
        setupSpinner()

        return binding.root
    }

    private fun setListeners() {
        binding.buttonAddPhoto.setOnClickListener { pickImageFromGallery() }
        binding.buttonSelectPlatforms.setOnClickListener { showPlatformDialog() }
        binding.buttonSubmitGame.setOnClickListener {
            if (validateInput()) submitGame()
        }
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.game_status_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerGameStatus.adapter = adapter
        }

        binding.spinnerGameStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                chosenStatus = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun showPlatformDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Escolha as plataformas")
            setMultiChoiceItems(platformsList, selectedPlatforms) { _, which, isChecked ->
                selectedPlatforms[which] = isChecked
            }
            setPositiveButton("OK") { _, _ -> updateSelectedPlatforms() }
            setNegativeButton("Cancelar", null)
        }.create().show()
    }

    private fun updateSelectedPlatforms() {
        chosenPlatforms = platformsList.filterIndexed { index, _ -> selectedPlatforms[index] }.joinToString()
        binding.textViewSelectedPlatforms.text = "Plataformas: $chosenPlatforms"
    }

    private fun pickImageFromGallery() {
        startActivityForResult(
            Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
            IMAGE_PICK_CODE
        )
    }

    private fun startCrop(uri: Uri) {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "JPEG_$timeStamp.jpg"
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, fileName))

        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1080, 1080)
            .start(requireContext(), this)
    }

    private fun validateInput(): Boolean {
        with(binding) {
            if (editTextGameTitle.text.isNullOrBlank()) {
                showToast("Por favor, insira um título para o jogo.")
                return false
            }
            if (chosenPlatforms.isBlank()) {
                showToast("Por favor, selecione pelo menos uma plataforma.")
                return false
            }
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            editTextGameYear.text.toString().toIntOrNull()?.let {
                if (it !in 1980..currentYear) {
                    showToast("Insira um ano de lançamento válido (1980 - $currentYear).")
                    return false
                }
            } ?: run {
                showToast("Insira um ano de lançamento válido (1980 - $currentYear).")
                return false
            }
            if ((editTextGameDuration.text.toString().toIntOrNull() ?: 0) <= 0) {
                showToast("Por favor, insira uma duração válida em horas.")
                return false
            }
        }
        return true
    }

    private fun clearForm() {
        with(binding) {
            editTextGameTitle.setText("")
            editTextGameDistributor.setText("")
            editTextGameYear.setText("")
            editTextGameDuration.setText("")
            editTextGameDescription.setText("")
            imageViewGameCover.setImageResource(R.drawable.ic_launcher_background)
            selectedPlatforms.fill(false)
            chosenPlatforms = ""
            textViewSelectedPlatforms.text = ""
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val IMAGE_PICK_CODE = 999
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_PICK_CODE -> data?.data?.let { startCrop(it) }
                UCrop.REQUEST_CROP -> {
                    imageUri = UCrop.getOutput(data!!)
                    binding.imageViewGameCover.setImageURI(imageUri)
                }
            }
        }
    }

    private fun submitGame() {
        val game = Gamemodel(
            titulo = binding.editTextGameTitle.text.toString().trim(),
            distribuidora = binding.editTextGameDistributor.text.toString().trim(),
            anoLancamento = binding.editTextGameYear.text.toString().toInt(),
            fotoUrl = imageUri?.toString() ?: "",
            plataformas = chosenPlatforms,
            miniTrailer = "",
            resumo = binding.editTextGameDescription.text.toString().trim(),
            tempoEstimado = binding.editTextGameDuration.text.toString().toInt(),
            status = chosenStatus
        )

        Log.d("AddGameFragment", "Adicionando jogo com status: $chosenStatus")

        val loggedInUserId = sharedPreferences.getInt("USER_ID", -1)
        lifecycleScope.launch {
            val id = withContext(Dispatchers.IO) { databaseHelper.addGame(game, loggedInUserId) }
            withContext(Dispatchers.Main) {
                if (id > 0) {
                    showToast("Jogo adicionado com sucesso!")
                    clearForm()
                } else {
                    showToast("Falha ao adicionar o jogo.")
                }
            }
        }
    }
}
