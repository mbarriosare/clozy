package com.clozy.app.data

import com.clozy.app.ui.wardrobe.Garment
import com.clozy.app.ui.wardrobe.GarmentCategory
import com.clozy.app.ui.wardrobe.GarmentColor
import com.clozy.app.ui.wardrobe.GarmentPattern
import com.clozy.app.ui.wardrobe.GarmentSeason
import com.clozy.app.ui.wardrobe.GarmentStyle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object WardrobeRepository {

    private val db get() = FirebaseFirestore.getInstance()
    private fun collection(uid: String) = db.collection("users").document(uid).collection("garments")

    suspend fun fetchAll(uid: String): List<Garment> {
        val snapshot = collection(uid).get().await()
        return snapshot.documents.mapNotNull { doc ->
            val data = doc.data ?: return@mapNotNull null
            Garment(
                id = doc.id.hashCode(),
                name = data["name"] as? String ?: "Prenda",
                category = (data["category"] as? String)?.let { GarmentCategory.valueOf(it) } ?: GarmentCategory.Top,
                primaryColor = (data["primaryColor"] as? String)?.let { GarmentColor.valueOf(it) } ?: GarmentColor.White,
                secondaryColors = (data["secondaryColors"] as? List<*>)?.mapNotNull { (it as? String)?.let { s -> GarmentColor.valueOf(s) } } ?: emptyList(),
                pattern = (data["pattern"] as? String)?.let { GarmentPattern.valueOf(it) } ?: GarmentPattern.Solid,
                isFavorite = data["isFavorite"] as? Boolean ?: false,
                style = (data["style"] as? String)?.let { GarmentStyle.valueOf(it) },
                season = (data["season"] as? String)?.let { GarmentSeason.valueOf(it) },
                usageCount = (data["usageCount"] as? Long)?.toInt() ?: 0,
                photoUrl = data["photoUrl"] as? String,
            ).also { docIds[it.id] = doc.id }
        }
    }

    private val docIds = mutableMapOf<Int, String>()

    suspend fun add(uid: String, garment: Garment): Garment {
        val ref = collection(uid).add(garment.toMap()).await()
        docIds[garment.id] = ref.id
        return garment
    }

    suspend fun update(uid: String, garment: Garment) {
        val docId = docIds[garment.id] ?: return
        collection(uid).document(docId).set(garment.toMap()).await()
    }

    suspend fun delete(uid: String, garment: Garment) {
        val docId = docIds[garment.id] ?: return
        collection(uid).document(docId).delete().await()
        docIds.remove(garment.id)
    }

    suspend fun deleteAll(uid: String) {
        val snapshot = collection(uid).get().await()
        for (doc in snapshot.documents) {
            doc.reference.delete().await()
        }
        docIds.clear()
    }

    private fun Garment.toMap(): Map<String, Any?> = mapOf(
        "name" to name,
        "category" to category.name,
        "primaryColor" to primaryColor.name,
        "secondaryColors" to secondaryColors.map { it.name },
        "pattern" to pattern.name,
        "isFavorite" to isFavorite,
        "style" to style?.name,
        "season" to season?.name,
        "usageCount" to usageCount,
        "photoUrl" to photoUrl,
    )
}
