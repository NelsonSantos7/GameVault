package com.example.gamevault.ui.NaLista

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamevault.model.GameStatus
import com.example.gamevault.SQLite.DBhelper
import com.example.gamevault.databinding.FragmentNaLitaListBinding
import com.example.gamevault.model.Gamemodel

class NaLita : Fragment() {

    private var _binding: FragmentNaLitaListBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbHelper: DBhelper
    private lateinit var viewAdapter: MyItemRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNaLitaListBinding.inflate(inflater, container, false)
        dbHelper = DBhelper(requireContext())

        val sharedPreferences = requireContext().getSharedPreferences("com.example.gamevault.prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("USER_ID", -1)

        val gamesInList = dbHelper.getGamesByStatusAndUser(GameStatus.NA_LISTA, userId)

        viewAdapter = MyItemRecyclerViewAdapter { game ->
            navigateToGameDetails(game)
        }

        binding.recyclerViewNaLita.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }

        viewAdapter.submitList(gamesInList)

        return binding.root
    }

    private fun navigateToGameDetails(game: Gamemodel) {
        if (isAdded) {
            val action = NaLitaDirections.actionNavNaListaToGameDetailsFragment(game)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
