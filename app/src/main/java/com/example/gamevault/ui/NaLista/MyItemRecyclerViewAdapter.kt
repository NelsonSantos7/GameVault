package com.example.gamevault.ui.NaLista

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gamevault.R
import com.example.gamevault.databinding.ItemGameBinding
import com.example.gamevault.model.Gamemodel
import com.squareup.picasso.Picasso

class MyItemRecyclerViewAdapter(
    private val onGameClicked: (Gamemodel) -> Unit
) : ListAdapter<Gamemodel, MyItemRecyclerViewAdapter.ViewHolder>(GameDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemGameBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onGameClicked
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: ItemGameBinding,
        private val onGameClicked: (Gamemodel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Gamemodel) {
            with(binding) {
                textViewGameTitle.text = game.titulo
                Picasso.get()
                    .load(game.fotoUrl)
                    .placeholder(R.drawable.ic_game_placeholder)
                    .error(R.drawable.ic_game_placeholder)
                    .into(imageViewGameCover)
                itemView.setOnClickListener { onGameClicked(game) }
            }
        }
    }

    class GameDiffCallback : DiffUtil.ItemCallback<Gamemodel>() {
        override fun areItemsTheSame(oldItem: Gamemodel, newItem: Gamemodel): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Gamemodel, newItem: Gamemodel): Boolean = oldItem == newItem
    }
}
