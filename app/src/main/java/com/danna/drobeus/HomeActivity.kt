package com.danna.drobeus

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.danna.drobeus.databinding.ActivityHomeBinding
import com.danna.drobeus.databinding.ItemCardBinding
import com.danna.drobeus.network.ApiClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = UserPrefs(this)

        // Si no hay sesión real (ej. sesión vieja), volver al login
        if (prefs.userId().isEmpty()) {
            prefs.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        binding.tvSaludo.text = "Hola, ${prefs.name().substringBefore(" ")}"
        val fecha = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(Date())
        binding.tvFecha.text = "$fecha · Semestre ${prefs.semester()}"

        val proximo = View.OnClickListener {
            Toast.makeText(this, "Disponible en el próximo sprint", Toast.LENGTH_SHORT).show()
        }
        binding.navMaterias.setOnClickListener(proximo)
        binding.navAgenda.setOnClickListener(proximo)
        binding.navNotas.setOnClickListener(proximo)

        cargarDatos(prefs.userId())
    }

    private fun cargarDatos(userId: String) {
        lifecycleScope.launch {
            try {
                // Clases de hoy
                val rClases = ApiClient.api.clasesDeHoy(userId, diaDeHoy())
                binding.listaClases.removeAllViews()
                val clases = rClases.body()?.clases ?: emptyList()
                if (clases.isEmpty()) {
                    agregarItem(binding.listaClases, "Sin clases hoy", "Disfruta tu día")
                        .dot.visibility = View.GONE
                }
                clases.forEach {
                    val detalle = "${it.horaInicio} – ${it.horaFin} · ${it.aula ?: ""}"
                    val item = agregarItem(binding.listaClases, it.materia, detalle)
                    try {
                        item.dot.backgroundTintList =
                            ColorStateList.valueOf(Color.parseColor(it.color ?: "#FFFFFF"))
                    } catch (e: Exception) { /* color inválido: se ignora */ }
                }

                // Tareas por entregar
                val rTareas = ApiClient.api.tareasPendientes(userId)
                binding.listaEntregas.removeAllViews()
                val tareas = rTareas.body()?.pendientes ?: emptyList()
                if (tareas.isEmpty()) {
                    agregarItem(binding.listaEntregas, "Sin tareas pendientes", "Estás al día")
                        .dot.visibility = View.GONE
                }
                tareas.forEach {
                    val detalle = "${it.materia ?: ""} · ${it.falta ?: ""}"
                    val item = agregarItem(binding.listaEntregas, it.titulo, detalle)
                    item.dot.visibility = View.GONE
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@HomeActivity,
                    "No se pudieron cargar los datos. Revisa tu internet.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // Devuelve el día en el formato que usa tu API: "lunes", "martes", etc.
    private fun diaDeHoy(): String {
        return when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "lunes"
            Calendar.TUESDAY -> "martes"
            Calendar.WEDNESDAY -> "miercoles"
            Calendar.THURSDAY -> "jueves"
            Calendar.FRIDAY -> "viernes"
            Calendar.SATURDAY -> "sabado"
            else -> "domingo"
        }
    }

    private fun agregarItem(padre: LinearLayout, titulo: String, detalle: String): ItemCardBinding {
        val item = ItemCardBinding.inflate(layoutInflater, padre, false)
        item.tvTitulo.text = titulo
        item.tvDetalle.text = detalle
        padre.addView(item.root)
        return item
    }
}