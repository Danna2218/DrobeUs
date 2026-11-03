package com.danna.drobeus

import android.content.Context

class UserPrefs(context: Context) {
    private val sp = context.getSharedPreferences("drobeus", Context.MODE_PRIVATE)

    fun register(name: String, email: String, pass: String) {
        sp.edit().putString("name", name).putString("email", email)
            .putString("pass", pass).apply()
    }

    fun login(email: String, pass: String): Boolean {
        val ok = email.equals(sp.getString("email", null), ignoreCase = true) &&
                pass == sp.getString("pass", null)
        if (ok) sp.edit().putBoolean("logged", true).apply()
        return ok
    }

    fun saveProfile(school: String, career: String, semester: String, schedule: String?) {
        sp.edit().putString("school", school).putString("career", career)
            .putString("semester", semester).putString("schedule", schedule)
            .putBoolean("onboarded", true).apply()
    }

    fun isLogged() = sp.getBoolean("logged", false)
    fun isOnboarded() = sp.getBoolean("onboarded", false)
    fun name() = sp.getString("name", "") ?: ""
    fun semester() = sp.getString("semester", "") ?: ""
}