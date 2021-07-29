package me.vanjavk.isa_shows_app_vanjavk.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import me.vanjavk.isa_shows_app_vanjavk.R
import me.vanjavk.isa_shows_app_vanjavk.databinding.ViewShowItemBinding

class ShowCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.cardview.R.attr.cardViewStyle
) : CardView(context, attrs, defStyleAttr) {

    private var binding: ViewShowItemBinding

    init {
//        inflate(context, R.layout.view_superhero_item, this)
        binding = ViewShowItemBinding.inflate(LayoutInflater.from(context), this)

        val horizontalPadding =
            context.resources.getDimensionPixelSize(R.dimen.card_padding_horizontal)
        val bottomPadding = context.resources.getDimensionPixelSize(R.dimen.card_padding_bottom)
        setPadding(horizontalPadding, 0, horizontalPadding, bottomPadding)
        val height = context.resources.getDimensionPixelSize(R.dimen.card_height)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, height)

        clipChildren = false
        clipToPadding = false
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


}