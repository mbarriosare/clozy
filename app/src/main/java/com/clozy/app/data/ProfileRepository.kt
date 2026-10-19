package com.clozy.app.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

data class StoredProfileFields(
    val name: String,
    val username: String,
    val city: String,
    val phone: String = "",
    val styles: List<String> = emptyList(),
    val colors: List<String> = emptyList(),
    val topSize: String = "",
    val bottomSize: String = "",
    val shoeSize: String = "",
)

object ProfileRepository {

    private val db get() = FirebaseFirestore.getInstance()
    private fun doc(uid: String) = db.collection("users").document(uid)

    suspend fun fetch(uid: String): StoredProfileFields? {
        val data = doc(uid).get().await().data ?: return null
        val name = data["name"] as? String ?: return null
        return StoredProfileFields(
            name = name,
            username = data["username"] as? String ?: "",
            city = data["city"] as? String ?: "",
            phone = data["phone"] as? String ?: "",
            styles = (data["styles"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
            colors = (data["colors"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
            topSize = data["topSize"] as? String ?: "",
            bottomSize = data["bottomSize"] as? String ?: "",
            shoeSize = data["shoeSize"] as? String ?: "",
        )
    }

    suspend fun deleteDoc(uid: String) {
        doc(uid).delete().await()
    }

    suspend fun save(uid: String, name: String, username: String, city: String, phone: String = "") {
        doc(uid).set(
            mapOf("name" to name, "username" to username, "city" to city, "phone" to phone),
            SetOptions.merge(),
        ).await()
    }

    suspend fun saveStyle(
        uid: String,
        styles: List<String>,
        colors: List<String>,
        topSize: String,
        bottomSize: String,
        shoeSize: String,
    ) {
        doc(uid).set(
            mapOf(
                "styles" to styles,
                "colors" to colors,
                "topSize" to topSize,
                "bottomSize" to bottomSize,
                "shoeSize" to shoeSize,
            ),
            SetOptions.merge(),
        ).await()
    }
}
