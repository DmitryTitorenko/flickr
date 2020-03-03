package com.example.flickr.data.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

object GlideUtils {

    fun loadImage(context: Context, url: String, imagePath: (String) -> Unit) {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val savedImagePath = SaveImageToStorageDir.save(context, resource)
                    imagePath(savedImagePath)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }
}
