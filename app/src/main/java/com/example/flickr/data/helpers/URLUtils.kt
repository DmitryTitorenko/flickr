package com.example.flickr.data.helpers

import com.example.flickr.model.ResponseImageList

object URLUtils {
    fun createURLForImage(list: ResponseImageList): String {
        val firstImage = list.photos?.photo?.get(1)
        return "https://farm" + firstImage?.farm + ".staticflickr.com/" + firstImage?.server + "/" + firstImage?.id + "_" + firstImage?.secret + ".jpg"
    }
}
