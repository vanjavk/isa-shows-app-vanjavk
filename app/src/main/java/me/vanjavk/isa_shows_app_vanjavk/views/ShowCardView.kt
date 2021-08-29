package me.vanjavk.isa_shows_app_vanjavk.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.cardview.widget.CardView
import me.vanjavk.isa_shows_app_vanjavk.databinding.ShowCardviewBinding

class ShowCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.cardview.R.attr.cardViewStyle
) : CardView(context, attrs, defStyleAttr) {

    private var binding: ShowCardviewBinding

    init {
        binding = ShowCardviewBinding.inflate(LayoutInflater.from(context), this)
    }

    fun setTitle(title: String) {
        binding.showTitle.text = title
    }

    fun setDescription(description: String) {
        binding.showDescription.text = description
    }

    fun getImageView(): ImageView {
        return binding.showImage
    }


}