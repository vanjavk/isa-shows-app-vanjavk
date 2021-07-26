package me.vanjavk.isa_shows_app_vanjavk

import Show
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import me.vanjavk.isa_shows_app_vanjavk.databinding.ViewShowItemBinding


class ShowsAdapter(
    private var items: List<Show>,
    private val onItemClickCallback: (Show) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {

        val binding = ViewShowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ShowViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(shows: List<Show>) {
        items = shows
        notifyDataSetChanged()
    }

    fun addItem(show: Show) {
        items = items + show
        notifyItemInserted(items.size)
    }

    inner class ShowViewHolder(private val binding: ViewShowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Show) {
            binding.showTitle.text = item.title
            Glide.with(itemView.context).load(item.imageUrl).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(binding.showImage)
            binding.showDescription.text = item.description
            binding.root.setOnClickListener {
                onItemClickCallback(item)
            }
        }
    }
}
