package com.example.gamevault.ui.Noticias

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamevault.databinding.FragmentNoticiasBinding
import com.example.gamevault.viewmodel.NoticiasViewModel

class NoticiasFragment : Fragment() {

    private var _binding: FragmentNoticiasBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NoticiasViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoticiasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        viewModel.fetchNoticias()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewNews.layoutManager = LinearLayoutManager(context)
    }

    private fun observeViewModel() {
        viewModel.noticias.observe(viewLifecycleOwner) { newsList ->
            binding.recyclerViewNews.adapter = NoticiasAdapter(newsList)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            // Handle the error message here
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
