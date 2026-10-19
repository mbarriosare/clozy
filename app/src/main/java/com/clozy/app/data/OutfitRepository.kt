package com.clozy.app.data

import com.clozy.app.ui.outfits.Outfit
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object OutfitRepository {

    private val db get() = FirebaseFirestore.getInstance()
    private fun collection(uid: String) = db.collection("users").document(uid).collection("outfits")
    private val docIds = mutableMapOf<Int, String>()

    suspend fun fetchAll(uid: String): List<Outfit> {
        val snapshot = collection(uid).get().await()
        return snapshot.documents.mapNotNull { doc ->
            val data = doc.data ?: return@mapNotNull null
            Outfit(
                id = doc.id.hashCode(),
                name = data["name"] as? String ?: "Outfit",
                occasion = data["occasion"] as? String ?: "",
                style = data["style"] as? String ?: "",
                garmentIds = (data["garmentIds"] as? List<*>)?.mapNotNull { (it as? Long)?.toInt() } ?: emptyList(),
                isFavorite = data["isFavorite"] as? Boolean ?: false,
                createdBy = data["createdBy"] as? String ?: "Tú",
            ).also { docIds[it.id] = doc.id }
        }
    }

    suspend fun add(uid: String, outfit: Outfit): Outfit {
        val ref = collection(uid).add(outfit.toMap()).await()
        docIds[outfit.id] = ref.id
        return outfit
    }

    suspend fun update(uid: String, outfit: Outfit) {
        val docId = docIds[outfit.id] ?: return
        collection(uid).document(docId).set(outfit.toMap()).await()
    }

    suspend fun delete(uid: String, outfit: Outfit) {
        val docId = docIds[outfit.id] ?: return
        collection(uid).document(docId).delete().await()
        docIds.remove(outfit.id)
    }

    private fun Outfit.toMap(): Map<String, Any?> = mapOf(
        "name" to name,
        "occasion" to occasion,
        "style" to style,
        "garmentIds" to garmentIds,
        "isFavorite" to isFavorite,
        "createdBy" to createdBy,
    )
}
