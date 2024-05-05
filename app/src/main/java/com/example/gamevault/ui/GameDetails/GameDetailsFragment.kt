package com.example.gamevault.ui.GameDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gamevault.R
import com.example.gamevault.SQLite.DBhelper
import com.example.gamevault.databinding.FragmentGameDetailsBinding
import com.example.gamevault.model.Gamemodel
import com.squareup.picasso.Picasso

class GameDetailsFragment : Fragment() {
    private var _binding: FragmentGameDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DBhelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameDetailsBinding.inflate(inflater, container, false)
        dbHelper = DBhelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gameId = arguments?.getInt("game_id") ?: -1 // Use -1 as default to indicate not found
        if (gameId != -1) {
            loadGameData(gameId)
        } else {
            showInvalidGameIdMessage()
        }
    }

    private fun loadGameData(gameId: Int) {
        val game = dbHelper.getGameById(gameId)
        if (game != null) {
            displayGameDetails(game)
        } else {
            showDataUnavailableMessage()
        }
    }

    private fun displayGameDetails(game: Gamemodel) {
        binding.textViewTitle.text = game.titulo
        binding.textViewDistributor.text = getString(R.string.distributor_format, game.distribuidora)
        binding.textViewReleaseYear.text = getString(R.string.release_year_format, game.anoLancamento)
        binding.textViewEstimatedTime.text = getString(R.string.estimated_time_format, game.tempoEstimado)
        binding.textViewPlatforms.text = getString(R.string.platforms_format, game.plataformas)
        binding.textViewStatus.text = getStatusText(game.status)

        Picasso.get().load(game.fotoUrl)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(binding.imageViewGameCover)
    }

    private fun getStatusText(status: Int): String {
        return when (status) {
            0 -> getString(R.string.status_playing)
            1 -> getString(R.string.status_will_play)
            2 -> getString(R.string.status_want_to_play)
            else -> getString(R.string.status_unknown)
        }
    }

    private fun showInvalidGameIdMessage() {
        binding.textViewTitle.text = getString(R.string.invalid_game_id)
        binding.imageViewGameCover.setImageResource(R.mipmap.ic_launcher)
    }

    private fun showDataUnavailableMessage() {
        binding.textViewTitle.text = getString(R.string.data_unavailable)
        binding.imageViewGameCover.setImageResource(R.mipmap.ic_launcher)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
