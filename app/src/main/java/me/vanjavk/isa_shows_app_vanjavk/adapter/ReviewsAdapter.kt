package me.vanjavk.isa_shows_app_vanjavk.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import me.vanjavk.isa_shows_app_vanjavk.R
import me.vanjavk.isa_shows_app_vanjavk.databinding.ItemReviewBinding
import me.vanjavk.isa_shows_app_vanjavk.getUsername
import me.vanjavk.isa_shows_app_vanjavk.model.Review

class ReviewsAdapter(
    private var items: List<Review>,
) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {

        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ReviewViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(reviews: List<Review>) {
        items = reviews
        notifyDataSetChanged()
    }

    fun addItem(review: Review) {
        items = listOf(review)+items
        notifyItemInserted(0)
    }

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Review) {
            binding.ratingStarValue.text = item.rating.toString()
            binding.reviewContent.isVisible = !item.comment.isNullOrEmpty()
            binding.reviewContent.text = item.comment
            binding.reviewerName.text = item.user.email.getUsername()
            if (item.user.imageUrl!=null){
                Glide.with(itemView.context).load(item.user.imageUrl).into(binding.profileIconImage)
            }else{
                binding.profileIconImage.setImageResource(R.drawable.ic_painting_art)
            }

        }
    }
}