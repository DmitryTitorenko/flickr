package com.example.flickr.data.repository

import com.example.flickr.data.network.API
import com.example.flickr.model.ResponseImageList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repo @Inject constructor(
    private val api: API
) {

    suspend fun search(text: String): ResponseImageList =
        withContext(Dispatchers.IO) {
            return@withContext api.search(
                "flickr.photos.search",
                "ad28c68cb8c7d88c2bc962ed5b9464a1",
                text,
                "json",
                "1"
            )
        }
}
