package com.example.flickr.data.network

import com.example.flickr.model.ResponseImageList
import retrofit2.http.GET
import retrofit2.http.Query

interface API {
    companion object {
        const val BASE_URL = "https://www.flickr.com/services/"
    }

    @GET("rest/")
    suspend fun search(
        @Query("method") method: String,
        @Query("api_key") api_key: String,
        @Query("text") text: String,
        @Query("format") format: String,
        @Query("nojsoncallback") nojsoncallback: String
    ): ResponseImageList
}
