package com.danna.drobeus

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.danna.drobeus.databinding.ActivityLoginBinding
import com.danna.drobeus.model.LoginRequest
import com.danna.drobeus.model.LoginResponse
import com.danna.drobeus.network.ApiClient
import com.google.gson.Gson
import kotlinx.coroutines.launch

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
                return@setOnClickListener
            }

            binding.btnEntrar.isEnabled = false
            binding.btnEntrar.text = "Entrando..."

            lifecycleScope.launch {
                try {
                    val respuesta = ApiClient.api.login(LoginRequest(correo, clave))
                    val cuerpo = respuesta.body()

                    if (respuesta.isSuccessful && cuerpo?.usuario != null) {
                        val u = cuerpo.usuario
                        prefs.saveSession(u.id, u.nombre, u.correo, u.periodoActual)
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        // El servidor respondió con error (ej. credenciales incorrectas)
                        val mensaje = try {
                            Gson().fromJson(
                                respuesta.errorBody()?.string(),
                                LoginResponse::class.java
                            )?.mensaje
                        } catch (e: Exception) {
                            null
                        }
                        Toast.makeText(
                            this@LoginActivity,
                            mensaje ?: "Correo o contraseña incorrectos",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@LoginActivity,
                        "No se pudo conectar con el servidor. Revisa tu internet.",
                        Toast.LENGTH_LONG
                    ).show()
                } finally {
                    binding.btnEntrar.isEnabled = true
                    binding.btnEntrar.text = "Entrar"
                }
            }
        }
    }
}