package com.example.gamevault.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamevault.MyApp
import com.example.gamevault.databinding.FragmentHome1Binding
import com.example.gamevault.model.Gamemodel
import com.example.gamevault.viewmodel.GameViewModel
import androidx.navigation.fragment.findNavController // Adicione este import

class HomeFragment1 : Fragment() {

    private var _binding: FragmentHome1Binding? = null
    private val binding get() = _binding!!

    private val gameViewModel: GameViewModel by viewModels {
        GameViewModelFactory((requireActivity().application as MyApp).repository)
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
        binding.gamesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.gamesRecyclerView.adapter = adapter
    }

    private fun subscribeUi() {
        gameViewModel.games.observe(viewLifecycleOwner) { games ->
            (binding.gamesRecyclerView.adapter as GameAdapter).submitList(games)
        }
    }

    private fun navigateToGameDetails(game: Gamemodel) {
        if (isAdded) {
            val action = HomeFragment1Directions.actionNavHomeToGameDetailsFragment(game)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
