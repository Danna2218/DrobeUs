package com.danna.drobeus.network

import com.danna.drobeus.model.ClasesResponse
import com.danna.drobeus.model.LoginRequest
import com.danna.drobeus.model.LoginResponse
import com.danna.drobeus.model.PendientesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("api/materias/hoy")
    suspend fun clasesDeHoy(
        @Query("usuario") usuario: String,
        @Query("dia") dia: String
    ): Response<ClasesResponse>

    @GET("api/tareas/pendientes")
    suspend fun tareasPendientes(
        @Query("usuario") usuario: String
    ): Response<PendientesResponse>
}