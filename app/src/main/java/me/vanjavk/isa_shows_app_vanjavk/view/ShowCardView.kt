package me.vanjavk.isa_shows_app_vanjavk.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import me.vanjavk.isa_shows_app_vanjavk.R
import me.vanjavk.isa_shows_app_vanjavk.databinding.ShowCardviewBinding
import me.vanjavk.isa_shows_app_vanjavk.databinding.ViewShowItemBinding

class ShowCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.cardview.R.attr.cardViewStyle
) : CardView(context, attrs, defStyleAttr) {

    private var binding: ShowCardviewBinding

    init {
//        inflate(context, R.layout.view_superhero_item, this)
        binding = ShowCardviewBinding.inflate(LayoutInflater.from(context), this)
//
//        val bottomPadding = context.resources.getDimensionPixelSize(R.dimen.card_padding_bottom)
//        setPadding(0, 0, 0, bottomPadding)
//        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
//        radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, context.resources.displayMetrics)
    }

    fun setTitle(title: String) {
        binding.showTitle.text = title
    }

    fun setDescription(description: String) {
        binding.showDescription.text = description
    }

    fun setImage(@DrawableRes imageResource: Int) {
        binding.showImage.setImageResource(imageResource)
    }

    fun getImageView(): ImageView {
        return binding.showImage
    }


}