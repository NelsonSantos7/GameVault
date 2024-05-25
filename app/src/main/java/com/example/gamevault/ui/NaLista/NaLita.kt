package com.example.gamevault.ui.NaLista

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamevault.MyApp
import com.example.gamevault.databinding.FragmentNaLitaBinding
import com.example.gamevault.model.GameStatus
import com.example.gamevault.model.Gamemodel
import com.example.gamevault.ui.home.GameRepository
import kotlinx.coroutines.launch

class NaLita : Fragment() {

    private var _binding: FragmentNaLitaBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: GameRepository
    private lateinit var viewAdapter: MyItemRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNaLitaBinding.inflate(inflater, container, false)

        repository = (requireActivity().application as MyApp).repository

        setupRecyclerView()
        loadGames()

        return binding.root
    }

    private fun setupRecyclerView() {
        viewAdapter = MyItemRecyclerViewAdapter { game ->
            navigateToGameDetails(game)
        }
        binding.recyclerViewNaLita.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }
    }

    private fun loadGames() {
        val sharedPreferences = requireContext().getSharedPreferences("com.example.gamevault.prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("USER_ID", null) ?: return

        lifecycleScope.launch {
            val gamesInList = repository.getGamesByStatusAndUser(GameStatus.WANT_TO_PLAY.status, userId)
            viewAdapter.submitList(gamesInList)
        }
    }

    private fun navigateToGameDetails(game: Gamemodel) {
        // Implementar a navegação para detalhes do jogo aqui
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
