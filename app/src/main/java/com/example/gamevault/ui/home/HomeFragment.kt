package com.example.gamevault.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gamevault.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Utilizando 'viewModels()' para delegar a inicialização do ViewModel
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Observando a LiveData no ViewModel para atualizar a UI
        homeViewModel.text.observe(viewLifecycleOwner) { text ->
            binding.textHome.text = text
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Limpeza do binding para evitar vazamentos de memória
        _binding = null
    }
}
