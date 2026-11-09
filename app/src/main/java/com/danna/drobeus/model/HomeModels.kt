package com.danna.drobeus.model

data class ClaseHoy(
    val materiaId: String,
    val materia: String,
    val profesor: String?,
    val aula: String?,
    val color: String?,
    val horaInicio: String,
    val horaFin: String
)

data class ClasesResponse(
    val ok: Boolean,
    val dia: String?,
    val total: Int?,
    val clases: List<ClaseHoy>?
)

data class TareaPendiente(
    val id: String,
    val titulo: String,
    val tipo: String?,
    val materia: String?,
    val color: String?,
    val fechaEntrega: String?,
    val diasRestantes: Int?,
    val falta: String?
)

data class PendientesResponse(
    val ok: Boolean,
    val total: Int?,
    val pendientes: List<TareaPendiente>?
)