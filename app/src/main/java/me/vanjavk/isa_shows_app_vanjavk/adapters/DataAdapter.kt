//package me.vanjavk.isa_shows_app_vanjavk.adapters
//
//import androidx.annotation.NonNull
//
//import androidx.recyclerview.widget.RecyclerView
//
//import android.R
//
//import android.view.LayoutInflater
//
//import android.view.ViewGroup
//
//
//class DataAdapter(mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//    private val mContext: Context
//    private var VIEW_TYPE = 0
//    fun setVIEW_TYPE(viewType: Int) {
//        VIEW_TYPE = viewType
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        var view: View? = null
//        when (VIEW_TYPE) {
//            ITEM_TYPE_GRID -> {
//                // if VIEW_TYPE is Grid than return GridViewHolder
//                view = LayoutInflater.from(mContext).inflate(R.layout.grid_layout, parent, false)
//                return GridViewHolder(view)
//            }
//            ITEM_TYPE_CARD_LIST -> {
//                // if VIEW_TYPE is Card List than return CardListViewHolder
//                view =
//                    LayoutInflater.from(mContext).inflate(R.layout.cardlist_layout, parent, false)
//                return CardListViewHolder(view)
//            }
//            ITEM_TYPE_TITLE_LIST -> {
//                // if VIEW_TYPE is Title List than return TitleListViewHolder
//                view = LayoutInflater.from(mContext).inflate(R.layout.title_layout, parent, false)
//                return TitleListViewHolder(view)
//            }
//        }
//        return GridViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val itemType = getItemViewType(position)
//        // First check here the View Type
//        // than set data based on View Type to your recyclerview item
//        if (itemType == ITEM_TYPE_GRID) {
//            if (holder is CardViewHolder) {
//                val viewHolder = holder as GridViewHolder
//                // write here code for your grid list
//            }
//        } else if (itemType == ITEM_TYPE_CARD_LIST) {
//            if (holder is CardViewHolder) {
//                val buttonViewHolder = holder as CardListViewHolder
//                // write here code for your grid list
//            }
//        } else if (itemType == ITEM_TYPE_TITLE_LIST) {
//            if (holder is CardViewHolder) {
//                val buttonViewHolder = holder as TitleListViewHolder
//                // write here code for your TitleListViewHolder
//            }
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return 40
//    }
//
//    // RecyclerView.ViewHolder class for gridLayoutManager
//    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
//
//    // RecyclerView.ViewHolder class for Card list View
//    inner class CardListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
//
//    // RecyclerView.ViewHolder class for Title list View
//    inner class TitleListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
//    companion object {
//        const val ITEM_TYPE_GRID = 0
//        const val ITEM_TYPE_CARD_LIST = 1
//        const val ITEM_TYPE_TITLE_LIST = 2
//    }
//
//    init {
//        this.mContext = mContext
//    }
//}