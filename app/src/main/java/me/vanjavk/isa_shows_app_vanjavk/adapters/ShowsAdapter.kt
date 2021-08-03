package me.vanjavk.isa_shows_app_vanjavk.adapters

import Show
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.vanjavk.isa_shows_app_vanjavk.databinding.ViewShowItemBinding
import me.vanjavk.isa_shows_app_vanjavk.utils.GlideUrlCustomCacheKey


class ShowsAdapter(
    private var items: List<Show>,
    private val onItemClickCallback: (Show) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {

        val binding =
            ViewShowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            binding.showItemCardView.setTitle(item.title)
            Glide.with(itemView.context).load(GlideUrlCustomCacheKey(item.imageUrl)).into(binding.showItemCardView.getImageView())
            binding.showItemCardView.setDescription(item.description.orEmpty())

            binding.root.setOnClickListener {
                onItemClickCallback(item)
            }
        }
    }




}