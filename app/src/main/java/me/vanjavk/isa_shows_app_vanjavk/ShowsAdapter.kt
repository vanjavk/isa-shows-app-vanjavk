package me.vanjavk.isa_shows_app_vanjavk

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.vanjavk.isa_shows_app_vanjavk.databinding.ViewShowItemBinding
import me.vanjavk.isa_shows_app_vanjavk.model.Show


class ShowsAdapter(
    private var items: List<Show>,
    private val onItemClickCallback: (Show) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    /**
     * Called when RecyclerView needs a new ViewHolder to represent an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        // Create a view from
        val binding = ViewShowItemBinding.inflate(LayoutInflater.from(parent.context))
        // Create a ViewHolder with that view
        return ShowViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * Called by the RecyclerView to display the data at the specified position.
     */
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

    /**
     * Custom-made ViewHolder, used to match the data to the concrete view.
     */
    inner class ShowViewHolder(private val binding: ViewShowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Show) {
            // Load the image
            binding.superheroImage.setImageResource(item.imageResourceId)

            // Load the name
            binding.superheroName.text = item.name

            binding.root.setOnClickListener {
                onItemClickCallback(item)
            }
        }
    }
}
