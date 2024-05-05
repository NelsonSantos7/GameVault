package com.example.gamevault.ui.AddGame

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.gamevault.R
import com.example.gamevault.SQLite.DBhelper
import com.example.gamevault.databinding.FragmentAddGameBinding
import com.example.gamevault.model.Gamemodel
import com.yalantis.ucrop.UCrop
import java.io.File

class AddGameFragment : Fragment() {
    private var _binding: FragmentAddGameBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DBhelper
    private var imageUri: Uri? = null
    private lateinit var selectedPlatforms: BooleanArray
    private lateinit var platformsList: Array<String>
    private var chosenPlatforms: String = ""
    private var chosenStatus: Int = 0 // Adicione esta linha

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddGameBinding.inflate(inflater, container, false)
        databaseHelper = DBhelper(requireContext())
        platformsList = resources.getStringArray(R.array.platform_options)
        selectedPlatforms = BooleanArray(platformsList.size)
        setListeners()
        setupSpinner()
        return binding.root
    }

    private fun setListeners() {
        binding.buttonAddPhoto.setOnClickListener {
            pickImageFromGallery()
        }
        binding.buttonSelectPlatforms.setOnClickListener {
            showPlatformDialog()
        }
        binding.buttonSubmitGame.setOnClickListener {
            submitGame(chosenStatus) // Corrija isso
        }
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.game_status_options,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGameStatus.adapter = adapter

        binding.spinnerGameStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                chosenStatus = position // Atualiza o status escolhido
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Não é necessário fazer nada aqui
            }
        }
    }

    private fun showPlatformDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Escolha as plataformas")
            setMultiChoiceItems(platformsList, selectedPlatforms) { _, which, isChecked ->
                selectedPlatforms[which] = isChecked
            }
            setPositiveButton("OK") { _, _ ->
                updateSelectedPlatforms()
            }
            setNegativeButton("Cancelar", null)
        }.create().show()
    }

    private fun updateSelectedPlatforms() {
        chosenPlatforms = platformsList.indices
            .filter { selectedPlatforms[it] }
            .joinToString { platformsList[it] }

        Toast.makeText(requireContext(), "Plataformas selecionadas: $chosenPlatforms", Toast.LENGTH_LONG).show()
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                startCrop(uri)
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            imageUri = UCrop.getOutput(data!!)
            binding.imageViewGameCover.setImageURI(imageUri)
        }
    }

    private fun startCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "IMG_${System.currentTimeMillis()}.jpg"))
        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(500, 500)
            .start(requireContext(), this)
    }

    private fun submitGame(status: Int) { // Adicione o parâmetro 'status' aqui
        if (chosenPlatforms.isBlank()) {
            Toast.makeText(requireContext(), "Por favor, selecione pelo menos uma plataforma.", Toast.LENGTH_SHORT).show()
            return
        }

        val game = Gamemodel(
            titulo = binding.editTextGameTitle.text.toString().trim(),
            distribuidora = binding.editTextGameDistributor.text.toString().trim(),
            anoLancamento = binding.editTextGameYear.text.toString().toIntOrNull() ?: 0,
            fotoUrl = imageUri?.toString() ?: "",
            plataformas = chosenPlatforms,
            miniTrailer = "",
            resumo = "",
            tempoEstimado = binding.editTextGameDuration.text.toString().toIntOrNull() ?: 0,
            status = status // Passa o valor do status aqui
        )

        val id = databaseHelper.addGame(game)
        if (id > 0) {
            Toast.makeText(requireContext(), getString(R.string.game_added_success), Toast.LENGTH_SHORT).show()
            clearForm()
        } else {
            Toast.makeText(requireContext(), getString(R.string.failed_to_add_game), Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearForm() {
        binding.editTextGameTitle.setText("")
        binding.editTextGameDistributor.setText("")
        binding.editTextGameYear.setText("")
        binding.editTextGameDuration.setText("")
        binding.imageViewGameCover.setImageResource(R.mipmap.ic_launcher)
        selectedPlatforms.fill(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val IMAGE_PICK_CODE = 999
    }
}
