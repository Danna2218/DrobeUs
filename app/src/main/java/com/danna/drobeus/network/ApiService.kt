package com.danna.drobeus.network

import com.danna.drobeus.model.LoginRequest
import com.danna.drobeus.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}