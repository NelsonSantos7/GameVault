package com.example.gamevault.ui.EmProgresso

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamevault.SQLite.DBhelper
import com.example.gamevault.databinding.FragmentEmProgressoListBinding
import com.example.gamevault.model.GameStatus
import com.example.gamevault.model.Gamemodel

class EmProgresso : Fragment() {

    private var _binding: FragmentEmProgressoListBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbHelper: DBhelper
    private lateinit var viewAdapter: MyItemRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmProgressoListBinding.inflate(inflater, container, false)
        dbHelper = DBhelper(requireContext())

        // Obtenha o ID do usuário logado
        val sharedPreferences = requireContext().getSharedPreferences("com.example.gamevault.prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("USER_ID", -1)

        // Use GameStatus.EM_PROGRESSO para buscar jogos com status "Em Progresso" do usuário logado
        val gamesInProgress = dbHelper.getGamesByStatusAndUser(GameStatus.EM_PROGRESSO, userId)

        viewAdapter = MyItemRecyclerViewAdapter { game ->
            navigateToGameDetails(game)
        }

        binding.list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }

        viewAdapter.submitList(gamesInProgress)

        return binding.root
    }

    private fun navigateToGameDetails(game: Gamemodel) {
        if (isAdded) {
            val action = EmProgressoDirections.actionNavEmProgressoToGameDetailsFragment(game)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
