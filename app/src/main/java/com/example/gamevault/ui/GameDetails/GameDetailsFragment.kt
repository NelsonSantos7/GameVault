package com.example.gamevault.ui.GameDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.gamevault.databinding.FragmentGameDetailsBinding
import com.example.gamevault.firebase.FirebaseHelper
import com.example.gamevault.model.GameStatus
import com.example.gamevault.model.Gamemodel
import com.example.gamevault.viewmodel.GameDetailsViewModel
import com.example.gamevault.viewmodel.GameDetailsViewModelFactory

class GameDetailsFragment : Fragment() {

    private var _binding: FragmentGameDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: GameDetailsFragmentArgs by navArgs()
    private val viewModel: GameDetailsViewModel by viewModels {
        GameDetailsViewModelFactory(FirebaseHelper())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val game = args.game
        displayGameDetails(game)

        binding.buttonUpdateStatusPlaying.setOnClickListener {
            updateGameStatus(game.id ?: "", GameStatus.PLAYING.status)
        }

        binding.buttonUpdateStatusWantToPlay.setOnClickListener {
            updateGameStatus(game.id ?: "", GameStatus.WANT_TO_PLAY.status)
        }

        binding.buttonUpdateStatusCompleted.setOnClickListener {
            updateGameStatus(game.id ?: "", GameStatus.COMPLETED.status)
        }
    }

    private fun displayGameDetails(game: Gamemodel) {
        binding.textViewGameTitle.text = game.title
        binding.textViewGameDistributor.text = game.distributor
        binding.textViewGameReleaseYear.text = game.releaseYear.toString()
        binding.textViewGameEstimatedTime.text = game.estimatedTime.toString()
        binding.textViewGameMetacriticScore.text = game.metacriticScore.toString()
    }

    private fun updateGameStatus(gameId: String, newStatus: Int) {
        viewModel.updateGameStatus(gameId, newStatus)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
