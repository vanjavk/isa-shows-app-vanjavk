package me.vanjavk.isa_shows_app_vanjavk.utils

import com.bumptech.glide.load.model.GlideUrl

class GlideUrlCustomCacheKey(url: String?) : GlideUrl(url) {
    override fun getCacheKey(): String = toURL().path
}