package com.example.gamevault.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamevault.databinding.FragmentHome1Binding
import com.example.gamevault.model.Gamemodel
import com.example.gamevault.MyApp


class HomeFragment1 : Fragment() {

    private var _binding: FragmentHome1Binding? = null
    private val binding get() = _binding!!

    private val gameViewModel: GameViewModel by viewModels {
        GameViewModelFactory((activity?.application as MyApp).repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHome1Binding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()
    }

    private fun setupRecyclerView() {
        val adapter = GameAdapter { game ->
            navigateToGameDetails(game)
        }
        binding.gamesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
    }

    private fun subscribeUi() {
        gameViewModel.games.observe(viewLifecycleOwner) { games ->
            (binding.gamesRecyclerView.adapter as GameAdapter).submitList(games)
        }
    }

    private fun navigateToGameDetails(game: Gamemodel) {
        val direction = HomeFragmentDirections.actionNavHomeToGameDetailsFragment(game.id ?: -1, game.titulo)
        findNavController().navigate(direction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic fun newInstance() = HomeFragment1()
    }
}

class GameViewModelFactory(private val repository: GameRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
