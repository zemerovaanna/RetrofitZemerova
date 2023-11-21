package com.example.moduleonevarianttwozemerova

import retrofit2.http.Body
import retrofit2.http.POST

interface MainApi {
    @POST("auth/login")
    suspend fun auth(@Body authRequest: AuthRequest): User
}