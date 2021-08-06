package me.vanjavk.isa_shows_app_vanjavk.adapters

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import me.vanjavk.isa_shows_app_vanjavk.models.Review


class ReviewDiff(var oldReviews: List<Review>, var  newReviews: List<Review>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldReviews.size
    }

    override fun getNewListSize(): Int {
        return newReviews.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldReviews[oldItemPosition].id === newReviews[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldReviews[oldItemPosition].id === newReviews[newItemPosition].id //oldReviews[oldItemPosition].equals(newReviews[newItemPosition])
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}