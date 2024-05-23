package com.example.gamevault.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gamevault.databinding.ItemGameBinding
import com.example.gamevault.model.Gamemodel

class GameAdapter(private val onGameClicked: (Gamemodel) -> Unit) :
    RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    private var games = listOf<Gamemodel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(games[position])
    }

    override fun getItemCount(): Int = games.size

    fun submitList(list: List<Gamemodel>) {
        games = list
        notifyDataSetChanged()
    }

    inner class GameViewHolder(private val binding: ItemGameBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(game: Gamemodel) {
            binding.textViewGameTitle.text = game.titulo
            // Configure other views in the item layout
            binding.root.setOnClickListener {
                onGameClicked(game)
            }
        }
    }
}
