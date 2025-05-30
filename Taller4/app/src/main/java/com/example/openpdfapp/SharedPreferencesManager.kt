package com.example.openpdfapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class UserRecord(
    val username: String,
    val loginTime: String
)

class SharedPreferencesManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val maxRecords = 3

    /**
     * Guarda un nuevo registro de usuario y mantiene solo los últimos 3 registros.
     * Si ya existen 3 registros, elimina el más antiguo.
     */
    fun saveUserData(username: String) {
        val currentTimeMillis = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.format(Date(currentTimeMillis))

        // Crear nuevo registro
        val newRecord = UserRecord(username, formattedDate)

        // Obtener registros existentes
        val existingRecords = getUserRecords().toMutableList()

        // Agregar el nuevo registro al inicio de la lista
        existingRecords.add(0, newRecord)

        // Mantener solo los últimos 3 registros
        while (existingRecords.size > maxRecords) {
            existingRecords.removeAt(existingRecords.size - 1)
        }

        // Guardar la lista actualizada
        saveUserRecords(existingRecords)
    }

    /**
     * Recupera todos los registros de usuario (máximo 3).
     * @return Lista de registros ordenados del más reciente al más antiguo.
     */
    fun getUserRecords(): List<UserRecord> {
        val json = sharedPreferences.getString("user_records", null)
        return if (json != null) {
            try {
                val type = object : TypeToken<List<UserRecord>>() {}.type
                gson.fromJson(json, type) ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    /**
     * Guarda la lista de registros en SharedPreferences.
     */
    private fun saveUserRecords(records: List<UserRecord>) {
        val json = gson.toJson(records)
        sharedPreferences.edit().apply {
            putString("user_records", json)
            apply()
        }
    }

    /**
     * Recupera el nombre de usuario del registro más reciente.
     * @return El nombre de usuario más reciente, o null si no existe.
     */
    fun getLatestUsername(): String? {
        val records = getUserRecords()
        return records.firstOrNull()?.username
    }

    /**
     * Recupera la fecha y hora de ingreso del registro más reciente.
     * @return La fecha y hora de ingreso más reciente, o null si no existe.
     */
    fun getLatestLoginTime(): String? {
        val records = getUserRecords()
        return records.firstOrNull()?.loginTime
    }

    /**
     * Recupera el nombre de usuario por índice (0 = más reciente, 1 = segundo más reciente, etc.).
     * @param index Índice del registro (0-2).
     * @return El nombre de usuario del índice especificado, o null si no existe.
     */
    fun getUsernameByIndex(index: Int): String? {
        val records = getUserRecords()
        return if (index in 0 until records.size) {
            records[index].username
        } else {
            null
        }
    }

    /**
     * Recupera la fecha y hora de ingreso por índice.
     * @param index Índice del registro (0-2).
     * @return La fecha y hora del índice especificado, o null si no existe.
     */
    fun getLoginTimeByIndex(index: Int): String? {
        val records = getUserRecords()
        return if (index in 0 until records.size) {
            records[index].loginTime
        } else {
            null
        }
    }

    /**
     * Limpia todos los registros de usuario de SharedPreferences.
     */
    fun clearUserData() {
        sharedPreferences.edit().clear().apply()
    }

    /**
     * Obtiene el número de registros guardados.
     * @return Número de registros (0-3).
     */
    fun getRecordCount(): Int {
        return getUserRecords().size
    }

    // Métodos de compatibilidad con la versión anterior
    @Deprecated("Usar getLatestUsername() en su lugar")
    fun getUsername(): String? = getLatestUsername()

    @Deprecated("Usar getLatestLoginTime() en su lugar")
    fun getLoginTime(): String? = getLatestLoginTime()
}