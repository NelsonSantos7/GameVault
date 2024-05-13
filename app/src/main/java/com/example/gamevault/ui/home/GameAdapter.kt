package com.example.gamevault.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.gamevault.R
import com.example.gamevault.databinding.ItemGameBinding
import com.example.gamevault.model.Gamemodel

class GameAdapter(private val onGameClicked: (Gamemodel) -> Unit) :
    ListAdapter<Gamemodel, GameAdapter.GameViewHolder>(GameDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding, onGameClicked)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = getItem(position)
        holder.bind(game)
        holder.itemView.setOnClickListener { onGameClicked(game) }
    }

    class GameViewHolder(private val binding: ItemGameBinding, onGameClicked: (Gamemodel) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(game: Gamemodel) {
            with(binding) {
                textViewGameTitle.text = game.titulo
                imageViewGameCover.loadImage(game.fotoUrl)
            }
        }

        private fun ImageView.loadImage(url: String?) {
            Glide.with(this)
                .load(url)
                .apply(RequestOptions()
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_baseline_error_24)
                    .diskCacheStrategy(DiskCacheStrategy.DATA))
                .into(this)
        }
    }

    class GameDiffCallback : DiffUtil.ItemCallback<Gamemodel>() {
        override fun areItemsTheSame(oldItem: Gamemodel, newItem: Gamemodel): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Gamemodel, newItem: Gamemodel): Boolean = oldItem == newItem
    }
}
