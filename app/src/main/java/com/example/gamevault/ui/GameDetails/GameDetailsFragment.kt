package com.example.gamevault.ui.GameDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gamevault.R
import com.example.gamevault.databinding.FragmentGameDetailsBinding
import com.example.gamevault.model.Gamemodel
import com.squareup.picasso.Picasso

class GameDetailsFragment : Fragment() {

    private var _binding: FragmentGameDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val game: Gamemodel? = arguments?.getParcelable("game")
        game?.let {
            displayGameDetails(it)
        }
    }

    private fun displayGameDetails(game: Gamemodel) {
        binding.textViewTitle.text = game.titulo
        binding.textViewDistributor.text = game.distribuidora
        binding.textViewReleaseYear.text = game.anoLancamento.toString()
        binding.textViewEstimatedTime.text = game.tempoEstimado.toString()
        binding.textViewPlatforms.text = game.plataformas
        binding.textViewStatus.text = when (game.status) {
            0 -> "Em Progresso"
            1 -> "Na Lista"
            2 -> "Finalizado"
            else -> "Desconhecido"
        }
        binding.textViewSummary.text = game.resumo
        // Carregar imagem
        Picasso.get()
            .load(game.fotoUrl)
            .placeholder(R.drawable.ic_game_placeholder)
            .error(R.drawable.ic_game_placeholder)
            .into(binding.imageViewGameCover)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
