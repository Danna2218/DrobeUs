package com.danna.drobeus

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.danna.drobeus.databinding.ActivityHomeBinding
import com.danna.drobeus.databinding.ItemCardBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Clase(val nombre: String, val detalle: String, val color: Int)
data class Entrega(val nombre: String, val detalle: String)

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = UserPrefs(this)
        binding.tvSaludo.text = "Hola, ${prefs.name().substringBefore(" ")}"
        val fecha = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(Date())
        binding.tvFecha.text = "$fecha · Semestre ${prefs.semester()}"

        val clases = listOf(
            Clase("Cálculo II", "7:00 – 9:00 · Aula 204", R.color.pink),
            Clase("Programación", "9:00 – 11:00 · Lab 3", R.color.sky),
            Clase("Física II", "2:00 – 4:00 · Aula 110", R.color.amber)
        )
        val entregas = listOf(
            Entrega("Taller de derivadas", "Cálculo II · Mañana"),
            Entrega("Parcial 1", "Física II · En 4 días"),
            Entrega("Informe de laboratorio", "Programación · En 6 días")
        )

        clases.forEach {
            val item = agregarItem(binding.listaClases, it.nombre, it.detalle)
            item.dot.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this, it.color))
        }

        entregas.forEach {
            val item = agregarItem(binding.listaEntregas, it.nombre, it.detalle)
            item.dot.visibility = View.GONE
        }

        val proximo = View.OnClickListener {
            Toast.makeText(this, "Disponible en el próximo sprint", Toast.LENGTH_SHORT).show()
        }
        binding.navMaterias.setOnClickListener(proximo)
        binding.navAgenda.setOnClickListener(proximo)
        binding.navNotas.setOnClickListener(proximo)
    }

    private fun agregarItem(padre: LinearLayout, titulo: String, detalle: String): ItemCardBinding {
        val item = ItemCardBinding.inflate(layoutInflater, padre, false)
        item.tvTitulo.text = titulo
        item.tvDetalle.text = detalle
        padre.addView(item.root)
        return item
    }
}