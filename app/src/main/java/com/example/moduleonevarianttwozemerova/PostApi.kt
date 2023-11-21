package com.example.moduleonevarianttwozemerova

import retrofit2.http.GET

interface PostApi {
    @GET("posts")
    suspend fun getAllPosts(): Posts
}