package com.example.openpdfapp

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SharedPreferencesManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)

    /**
     * Guarda el nombre de usuario y la fecha/hora de ingreso en SharedPreferences.
     * La fecha y hora se guardan como una cadena legible.
     */
    fun saveUserData(username: String) {
        val currentTimeMillis = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.format(Date(currentTimeMillis))

        sharedPreferences.edit().apply {
            putString("username", username)
            putString("login_time", formattedDate)
            apply() // Usa apply() para guardar de forma asíncrona
        }
    }

    /**
     * Recupera el nombre de usuario de SharedPreferences.
     * @return El nombre de usuario guardado, o una cadena vacía si no existe.
     */
    fun getUsername(): String? {
        return sharedPreferences.getString("username", null)
    }

    /**
     * Recupera la fecha y hora de ingreso de SharedPreferences.
     * @return La fecha y hora de ingreso guardada como String, o null si no existe.
     */
    fun getLoginTime(): String? {
        return sharedPreferences.getString("login_time", null)
    }

    /**
     * Limpia todos los datos de usuario de SharedPreferences (útil para un "logout" o reinicio).
     */
    fun clearUserData() {
        sharedPreferences.edit().clear().apply()
    }
}