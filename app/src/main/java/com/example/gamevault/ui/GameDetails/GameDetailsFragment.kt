package com.example.gamevault.ui.GameDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGameDetailsBinding.inflate(inflater, container, false)
        dbHelper = DBhelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gameId = arguments?.getInt("game_id") ?: -1
        if (gameId != -1) {
            loadGameData(gameId)
        } else {
            showInvalidGameIdMessage()
        }
    }

    private fun loadGameData(gameId: Int) {
        val game = dbHelper.getGameById(gameId)
        game?.let { displayGameDetails(it) } ?: showDataUnavailableMessage()
    }

    private fun displayGameDetails(game: Gamemodel) {
        with(binding) {
            textViewTitle.text = game.titulo
            textViewDistributor.text = getString(R.string.distributor_format, game.distribuidora)
            textViewReleaseYear.text = getString(R.string.release_year_format, game.anoLancamento)
            textViewEstimatedTime.text = getString(R.string.estimated_time_format, game.tempoEstimado)
            textViewPlatforms.text = getString(R.string.platforms_format, game.plataformas)
            textViewStatus.text = getStatusText(game.status)

            Picasso.get()
                .load(game.fotoUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageViewGameCover)
        }
    }

    private fun getStatusText(status: Int): String {
        return resources.getStringArray(R.array.game_status_options)[status]
    }

    private fun showInvalidGameIdMessage() {
        Toast.makeText(context, R.string.invalid_game_id, Toast.LENGTH_LONG).show()
    }

    private fun showDataUnavailableMessage() {
        Toast.makeText(context, R.string.data_unavailable, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
