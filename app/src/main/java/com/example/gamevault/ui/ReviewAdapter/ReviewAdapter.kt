package com.example.gamevault.ui.GameDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gamevault.databinding.ItemReviewBinding
import com.example.gamevault.model.ReviewModel

class ReviewAdapter(
    private val reviews: List<ReviewModel>
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int = reviews.size

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ReviewModel) {
            binding.textViewReviewUser.text = "User ${review.userId}" // Modifique para mostrar o nome do usuário se disponível
            binding.textViewReviewRating.text = "Rating: ${review.rating} estrelas"
            binding.textViewReviewComment.text = review.comment
        }
    }
}
