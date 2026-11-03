package com.danna.drobeus

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.danna.drobeus.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = UserPrefs(this)
        Handler(Looper.getMainLooper()).postDelayed({
            val destino = when {
                prefs.isLogged() && prefs.isOnboarded() -> HomeActivity::class.java
                prefs.isLogged() -> OnboardingActivity::class.java
                else -> LoginActivity::class.java
            }
            startActivity(Intent(this, destino))
            finish()
        }, 2000)
    }
}