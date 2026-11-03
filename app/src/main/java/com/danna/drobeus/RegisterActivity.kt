package com.danna.drobeus

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.danna.drobeus.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = UserPrefs(this)

        binding.tvIrLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnRegistrar.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            val correo = binding.etCorreo.text.toString().trim()
            val clave = binding.etClave.text.toString()

            when {
                nombre.isEmpty() -> binding.etNombre.error = "Escribe tu nombre"
                !Patterns.EMAIL_ADDRESS.matcher(correo).matches() ->
                    binding.etCorreo.error = "Correo no válido"
                clave.length < 6 -> binding.etClave.error = "Mínimo 6 caracteres"
                else -> {
                    prefs.register(nombre, correo, clave)
                    prefs.login(correo, clave)
                    Toast.makeText(this, "Cuenta creada", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, OnboardingActivity::class.java))
                    finish()
                }
            }
        }
    }
}