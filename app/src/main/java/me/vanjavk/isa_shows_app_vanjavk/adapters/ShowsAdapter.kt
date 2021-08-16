package me.vanjavk.isa_shows_app_vanjavk.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.vanjavk.isa_shows_app_vanjavk.databinding.ViewShowItemBinding
import me.vanjavk.isa_shows_app_vanjavk.databinding.ViewShowItemGridBinding
import me.vanjavk.isa_shows_app_vanjavk.models.Show
import me.vanjavk.isa_shows_app_vanjavk.utils.GlideUrlCustomCacheKey
import java.lang.Exception


class ShowsAdapter(
    private var items: List<Show>,
    private val onItemClickCallback: (Show) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ViewType {
        VIEW_TYPE_CARD, VIEW_TYPE_GRID
    }

    private var viewType: ViewType = ViewType.VIEW_TYPE_GRID

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.VIEW_TYPE_CARD.ordinal -> ShowViewHolder(
                ViewShowItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            ViewType.VIEW_TYPE_GRID.ordinal -> ShowViewHolderGrid(
                ViewShowItemGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw Exception("Invalid view type.")
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (viewType) {
            ViewType.VIEW_TYPE_CARD -> {
                if (holder is ShowViewHolder) {
                    holder.bind(items[position])
                }
            }
            ViewType.VIEW_TYPE_GRID -> {
                if (holder is ShowViewHolderGrid) {
                    holder.bind(items[position])
                }
            }
        }
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
            Glide.with(itemView.context).load(GlideUrlCustomCacheKey(item.imageUrl))
                .into(binding.showItemCardView.getImageView())
            binding.showItemCardView.setDescription(item.description.orEmpty())

            binding.root.setOnClickListener {
                onItemClickCallback(item)
            }
        }
    }

    inner class ShowViewHolderGrid(private val binding: ViewShowItemGridBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Show) {
            binding.showTitle.text = item.title
            Glide.with(itemView.context).load(GlideUrlCustomCacheKey(item.imageUrl))
                .into(binding.showImage)

            binding.root.setOnClickListener {
                onItemClickCallback(item)
            }
        }
    }


}
