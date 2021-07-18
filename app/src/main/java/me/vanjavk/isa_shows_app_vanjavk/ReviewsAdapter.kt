package me.vanjavk.isa_shows_app_vanjavk

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.vanjavk.isa_shows_app_vanjavk.databinding.ItemReviewBinding
import me.vanjavk.isa_shows_app_vanjavk.model.Review

class ReviewsAdapter(
    private var items: List<Review>,
) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {

        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context))
        //kao neki fix

        //s obzirom da nemogu staviti height u dp stavio sam sve u jos jedan framelayout
//        binding.root.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)

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
        items = items + review
        notifyItemInserted(items.size)
    }

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Review) {

            binding.ratingStarValue.text = item.stars.toString()
            binding.reviewerName.text = item.name

            if (item.content.isEmpty()) {
                binding.reviewContent.visibility = GONE
            } else {
                binding.reviewContent.text = item.content
            }

        }
    }
}