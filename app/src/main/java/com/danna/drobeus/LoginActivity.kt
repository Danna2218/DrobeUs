package com.danna.drobeus

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.danna.drobeus.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = UserPrefs(this)

        binding.tvIrRegistro.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        binding.btnEntrar.setOnClickListener {
            val correo = binding.etCorreo.text.toString().trim()
            val clave = binding.etClave.text.toString()

            if (correo.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Completa correo y contraseña", Toast.LENGTH_SHORT).show()
            } else if (prefs.login(correo, clave)) {
                val destino = if (prefs.isOnboarded()) HomeActivity::class.java
                else OnboardingActivity::class.java
                startActivity(Intent(this, destino))
                finish()
            } else {
                Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}