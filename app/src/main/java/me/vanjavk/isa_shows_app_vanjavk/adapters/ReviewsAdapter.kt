package me.vanjavk.isa_shows_app_vanjavk.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.vanjavk.isa_shows_app_vanjavk.R
import me.vanjavk.isa_shows_app_vanjavk.databinding.ItemReviewBinding
import me.vanjavk.isa_shows_app_vanjavk.getUsername
import me.vanjavk.isa_shows_app_vanjavk.models.Review
import me.vanjavk.isa_shows_app_vanjavk.utils.GlideUrlCustomCacheKey

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
        val diffResult = DiffUtil.calculateDiff(ReviewDiff(items, reviews))
        items = reviews
        diffResult.dispatchUpdatesTo(this)
        //notifyDataSetChanged()
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

            binding.frameLayout.setBackgroundColor( if (item.sync) Color.parseColor("#ffffff") else Color.parseColor("#808080"))
            if (item.user.imageUrl!=null){
                Glide.with(itemView.context).load(GlideUrlCustomCacheKey(item.user.imageUrl)).into(binding.profileIconImage)
            }else{
                binding.profileIconImage.setImageResource(R.drawable.ic_painting_art)
            }

        }
    }
}