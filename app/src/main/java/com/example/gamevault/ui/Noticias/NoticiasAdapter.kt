package com.example.gamevault.ui.Noticias

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gamevault.databinding.ItemNoticiasBinding
import com.example.gamevault.model.NoticiasModel
import com.squareup.picasso.Picasso

class NoticiasAdapter(private val newsList: List<NoticiasModel>) : RecyclerView.Adapter<NoticiasAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(private val binding: ItemNoticiasBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: NoticiasModel) {
            with(binding) {
                textViewTitle.text = news.title
                textViewDescription.text = news.content
                Picasso.get().load(news.imageUrl).into(imageViewNews)
                root.setOnClickListener {
                    // Implementar a ação ao clicar na notícia
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNoticiasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount() = newsList.size
}
