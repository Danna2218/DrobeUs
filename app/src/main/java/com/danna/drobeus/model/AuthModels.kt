package com.danna.drobeus.model

data class Usuario(
    val id: String,
    val nombre: String,
    val correo: String,
    val periodoActual: String
)

data class LoginRequest(
    val correo: String,
    val contrasena: String
)

data class LoginResponse(
    val ok: Boolean,
    val mensaje: String,
    val usuario: Usuario?
)