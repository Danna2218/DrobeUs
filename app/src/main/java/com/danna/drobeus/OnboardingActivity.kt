package com.danna.drobeus

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.danna.drobeus.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private var horarioUri: Uri? = null

    private val selector = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            horarioUri = uri
            binding.btnHorario.text = "Horario cargado ✓"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = UserPrefs(this)

        binding.btnHorario.setOnClickListener {
            selector.launch("*/*")
        }

        binding.btnIniciar.setOnClickListener {
            val uni = binding.etUniversidad.text.toString().trim()
            val carrera = binding.etCarrera.text.toString().trim()
            val semestre = binding.etSemestre.text.toString().trim()

            if (uni.isEmpty() || carrera.isEmpty() || semestre.isEmpty()) {
                Toast.makeText(this, "Completa todos los datos", Toast.LENGTH_SHORT).show()
            } else {
                prefs.saveProfile(uni, carrera, semestre, horarioUri?.toString())
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
    }
}