package com.example.concesionaria.repository

import com.example.concesionaria.model.User as LocalUser
import android.util.Log
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.example.concesionaria.model.UserDao
import kotlinx.coroutines.suspendCancellableCoroutine
import com.amplifyframework.datastore.generated.model.User as AmplifyUser
import kotlin.coroutines.resume


class UserRepository(private val userDao: UserDao) {

//    suspend fun registerUser(user: User): Boolean {
//        return try {
//            userDao.insert(user)
//            true
//        } catch (e: Exception) {
//            false
//        }
//    }
//
//    suspend fun loginUser(name: String, password: String): User? {
//        return userDao.getUser(name, password)
//    }


//    antiguos metodos de registro local solo cambia el nombre
    suspend fun registerLocalUser(user: LocalUser): Boolean {
        return try {
            userDao.insert(user)
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Error registering local user", e)
            false
        }
    }

    suspend fun loginLocalUser(name: String, password: String): LocalUser? {
        return userDao.getUser(name, password)
    }


//    registro y query en remoto base proporcionada por CLI de Amplify
    suspend fun registerRemoteUser(fullName: String, password: String): Boolean {
        val model = AmplifyUser.builder()
            .fullName(fullName)
            .password(password)
            .build()

        return try {
            suspendCancellableCoroutine { cont ->
                Amplify.API.mutate(ModelMutation.create(model),
                    { response ->
                        if (response.hasErrors()) {
                            Log.e("UserRepository", "Amplify error: ${response.errors.first().message}")
                            cont.resume(false)
                        } else {
                            cont.resume(true)
                        }
                    },
                    { error ->
                        Log.e("UserRepository", "Amplify mutation failed", error)
                        cont.resume(false)
                    }
                )
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error registering remote user", e)
            false
        }
    }

    suspend fun loginRemoteUser(fullName: String, password: String): AmplifyUser? {
        return try {
            suspendCancellableCoroutine { cont ->
                Amplify.API.query(ModelQuery.list(AmplifyUser::class.java),
                    { response ->
                        val user = response.data.items.firstOrNull {
                            it.fullName == fullName && it.password == password
                        }
                        cont.resume(user)
                    },
                    { error ->
                        Log.e("UserRepository", "Amplify query failed", error)
                        cont.resume(null)
                    }
                )
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error logging in remotely", e)
            null
        }
    }

//    Sincronizacion de usuarios

    suspend fun syncUserToLocal(user: AmplifyUser) {
        try {
            val localUser = LocalUser(fullName = user.fullName, password = user.password)
            userDao.insert(localUser)
        } catch (e: Exception) {
            Log.e("UserRepository", "Error syncing user to local DB", e)
        }
    }
}